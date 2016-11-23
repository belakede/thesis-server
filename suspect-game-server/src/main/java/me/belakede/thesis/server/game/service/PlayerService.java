package me.belakede.thesis.server.game.service;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import me.belakede.thesis.game.Game;
import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.server.game.converter.GameConverter;
import me.belakede.thesis.server.game.converter.PlayerConverter;
import me.belakede.thesis.server.game.domain.NullPlayer;
import me.belakede.thesis.server.game.domain.Player;
import me.belakede.thesis.server.game.domain.PlayerCard;
import me.belakede.thesis.server.game.exception.InvalidPlayerConfiguration;
import me.belakede.thesis.server.game.repository.PlayerRepository;
import me.belakede.thesis.server.game.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class PlayerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerService.class);

    private final MapProperty<Figurine, Player> players = new SimpleMapProperty<>();
    private final ListProperty<Player> connectedPlayers = new SimpleListProperty<>();
    private final ListProperty<Player> blockedPlayers = new SimpleListProperty<>();
    private final ObjectProperty<Player> currentPlayer = new SimpleObjectProperty<>();
    private final NotificationService notificationService;
    private final PlayerRepository playerRepository;
    private final PlayerConverter playerConverter;
    private final GameConverter gameConverter;
    private final GameService gameService;

    @Autowired
    public PlayerService(NotificationService notificationService, PlayerRepository playerRepository, PlayerConverter playerConverter, GameConverter gameConverter, GameService gameService) {
        this.notificationService = notificationService;
        this.playerRepository = playerRepository;
        this.playerConverter = playerConverter;
        this.gameConverter = gameConverter;
        this.gameService = gameService;
        setPlayers(FXCollections.observableHashMap());
        setConnectedPlayers(FXCollections.observableArrayList());
        setBlockedPlayers(FXCollections.observableArrayList());
        setCurrentPlayer(new NullPlayer());
        hookupChangeListeners();
    }

    Player getCurrentPlayer() {
        return currentPlayer.get();
    }

    private void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer.set(currentPlayer);
    }

    Player getNextPlayer() {
        return getPlayers().get(getNextFigurine());
    }

    public String getWinner() {
        String result = null;
        Game gameLogic = gameService.getGameLogic();
        if (gameLogic.isGameEnded()) {
            if (gameLogic.getCurrentPlayer().hasBeenMadeGroundlessAccusation()) {
                result = getNextPlayer().getUsername();
            } else {
                result = getCurrentPlayer().getUsername();
            }
        }
        return result;
    }

    SseEmitter join(String username) {
        Player player = updatePlayer(findPlayer(username));
        LOGGER.debug("{} has been mapped to {}", username, player);
        if (!blockedPlayers.contains(player)) {
            SseEmitter channel = notificationService.openChannelForUser(username);
            getConnectedPlayers().add(player);
            return channel;
        }
        throw new InvalidPlayerConfiguration("There is no room for this player.");
    }

    void next() {
        Suspect newCurrentPlayer = gameService.getGameLogic().getCurrentPlayer().getFigurine();
        Player currentPlayer = getPlayers().get(newCurrentPlayer);
        updateRepository(currentPlayer);
        setCurrentPlayer(currentPlayer);
    }

    void kill() {
        getCurrentPlayer().setAlive(false);
        updatePlayer(getCurrentPlayer());
        getBlockedPlayers().add(getCurrentPlayer());
        setCurrentPlayer(getNextPlayer());
    }

    void finish() {
        getPlayers().clear();
        getBlockedPlayers().clear();
        setCurrentPlayer(null);
    }

    boolean isValidPlayer(String username) {
        return getConnectedPlayers().stream().map(Player::getUsername).filter(username::equals).findFirst().isPresent();
    }

    private void hookupChangeListeners() {
        gameService.availableProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.debug("Running property changed to {} from {}", newValue, oldValue);
            if (newValue) {
                initPlayers();
                initBlockedPlayers();
            } else {
                clearPlayers();
                clearBlockedPlayers();
            }
            setConnectedPlayers(FXCollections.observableArrayList());
        });
        notificationService.missingUsersProperty().addListener((Change<? extends String> change) -> {
            while (change.next()) {
                List<Player> missingPlayers = change.getAddedSubList().stream()
                        .map(username -> getPlayers().values().stream().filter(player -> username.equals(player.getUsername())).findFirst())
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
                getConnectedPlayers().removeAll(missingPlayers);
            }
        });
        connectedPlayersProperty().addListener((Change<? extends Player> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(player -> {
                        broadcastPlayerJoined(player);
                        sendPlayerStatus(player);
                    });
                    if (getConnectedPlayers().size() == getPlayers().size()) {
                        LOGGER.info("Players are ready. Let's play.");
                        changeGameRunningState();
                        broadcastGameStatus();
                        initCurrentPlayer();
                    }
                } else {
                    change.getRemoved().forEach(player -> {
                        if (!blockedPlayers.contains(player)) {
                            broadcastGameHasBeenPaused();
                            pauseGame();
                            resetNotificationService();
                            finish();
                        }
                    });
                }
            }
        });
        blockedPlayersProperty().addListener((Change<? extends Player> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(player -> {
                        broadcastPlayerOut(player);
                        closeChannelForPlayer(player);
                    });
                }
            }
        });
        currentPlayerProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                broadcastCurrentPlayer();
            }
        });
    }

    private void changeGameRunningState() {
        gameService.setRunning(true);
    }

    private void broadcastPlayerJoined(Player player) {
        notificationService.broadcast(new PlayerJoinedNotification(player.getUsername()));
    }

    private void sendPlayerStatus(Player player) {
        PlayerStatusNotification notification = playerConverter.convert(player);
        notification.setAlreadyWaiting(getConnectedPlayers().stream().map(Player::getUsername).collect(Collectors.toSet()));
        notificationService.notify(player.getUsername(), notification);
    }

    private void broadcastGameStatus() {
        GameStatusNotification notification = gameConverter.convert(gameService.getGameEntity(), getConnectedPlayers());
        notificationService.broadcast(notification);
    }

    private void broadcastCurrentPlayer() {
        Notification notification = new CurrentPlayerNotification(getCurrentPlayer().getUsername(), getNextPlayer().getUsername(), gameService.getLastAction());
        notificationService.broadcast(notification);
    }

    private void pauseGame() {
        gameService.pauseTheGame();
    }

    private void broadcastGameHasBeenPaused() {
        notificationService.broadcast(new GamePausedNotification());
    }

    private void resetNotificationService() {
        notificationService.clear();
    }

    private void broadcastPlayerOut(Player player) {
        notificationService.broadcast(new PlayerOutNotification(player.getUsername(), player.getCards().stream().map(PlayerCard::getCard).collect(Collectors.toSet())));
    }

    private void closeChannelForPlayer(Player player) {
        notificationService.getMissingUsers().add(player.getUsername());
    }

    private void initCurrentPlayer() {
        Optional<Player> player = getPlayers().values().stream().filter(Player::isCurrent).findFirst();
        if (!player.isPresent()) {
            player = getPlayers().values().stream().min((o1, o2) -> o1.getOrdinalNumber().compareTo(o2.getOrdinalNumber()));
        }
        gameService.setCurrent(player.get().getFigurine());
        setCurrentPlayer(player.get());
    }

    private void clearPlayers() {
        getPlayers().clear();
    }

    private void initPlayers() {
        gameService.getGameEntity().getPlayers().stream().filter(Player::isAlive).forEach(player -> getPlayers().put(player.getFigurine(), player));
    }

    private void initBlockedPlayers() {
        gameService.getGameEntity().getPlayers().stream().filter(player -> !player.isAlive()).forEach(player -> getBlockedPlayers().add(player));
    }

    private void clearBlockedPlayers() {
        getBlockedPlayers().clear();
    }

    private void updateRepository(Player currentPlayer) {
        getPlayers().values().forEach(p -> p.setCurrent(p.equals(currentPlayer)));
        gameService.getGameEntity().setPlayers(new ArrayList<>(getPlayers().values()));
        playerRepository.save(getPlayers().values());
        gameService.changeLastAction(null);
    }

    private Player updatePlayer(Player player) {
        return playerRepository.save(player);
    }

    private Player findPlayer(String username) {
        Optional<Player> player = findPlayerByUsername(username);
        if (!player.isPresent()) {
            player = findFistFreePlayerSlot(username);
        }
        return player.get();
    }

    private Optional<Player> findPlayerByUsername(String username) {
        return getPlayers().values().stream().filter(player -> username.equals(player.getUsername())).findFirst();
    }

    private Optional<Player> findFistFreePlayerSlot(String username) {
        Optional<Player> freePlayerSlot = getPlayers().values().stream().filter(player -> null == player.getUsername() || "".equals(player.getUsername())).findAny();
        if (!freePlayerSlot.isPresent()) {
            throw new InvalidPlayerConfiguration("There is no room for this player.");
        } else {
            freePlayerSlot.get().setUsername(username);
        }
        return freePlayerSlot;
    }

    private Suspect getNextFigurine() {
        return gameService.getGameLogic().getNextPlayer().getFigurine();
    }

    private ObservableMap<Figurine, Player> getPlayers() {
        return players.get();
    }

    private void setPlayers(ObservableMap<Figurine, Player> players) {
        this.players.set(players);
    }

    private ObservableList<Player> getConnectedPlayers() {
        return connectedPlayers.get();
    }

    private void setConnectedPlayers(ObservableList<Player> connectedPlayers) {
        this.connectedPlayers.set(connectedPlayers);
    }

    private ListProperty<Player> connectedPlayersProperty() {
        return connectedPlayers;
    }

    private ObservableList<Player> getBlockedPlayers() {
        return blockedPlayers.get();
    }

    private void setBlockedPlayers(ObservableList<Player> blockedPlayers) {
        this.blockedPlayers.set(blockedPlayers);
    }

    private ListProperty<Player> blockedPlayersProperty() {
        return blockedPlayers;
    }

    private ObjectProperty<Player> currentPlayerProperty() {
        return currentPlayer;
    }
}
