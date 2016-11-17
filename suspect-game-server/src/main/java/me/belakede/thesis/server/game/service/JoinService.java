package me.belakede.thesis.server.game.service;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import me.belakede.thesis.server.game.converter.PlayerConverter;
import me.belakede.thesis.server.game.domain.NullPlayer;
import me.belakede.thesis.server.game.domain.Player;
import me.belakede.thesis.server.game.exception.InvalidPlayerConfiguration;
import me.belakede.thesis.server.game.repository.PlayerRepository;
import me.belakede.thesis.server.game.response.PlayerJoinedNotification;
import me.belakede.thesis.server.game.response.PlayerStatusNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Optional;

@Service
public class JoinService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JoinService.class);

    private final PlayerRepository playerRepository;
    private final PlayerConverter playerConverter;
    private final GameLogicService gameLogicService;
    private final NotificationService notificationService;
    private final ObservableMap<String, Player> players;
    private final NullPlayer nullPlayer;

    @Autowired
    public JoinService(PlayerRepository playerRepository, PlayerConverter playerConverter, GameLogicService gameLogicService, NotificationService notificationService) {
        this.playerRepository = playerRepository;
        this.playerConverter = playerConverter;
        this.gameLogicService = gameLogicService;
        this.notificationService = notificationService;
        this.players = FXCollections.observableHashMap();
        this.nullPlayer = new NullPlayer();
        hookupChangeListeners();
    }

    public SseEmitter join(String username) {
        LOGGER.info("{} tries to join.", username);
        SseEmitter emitter = notificationService.createEmitter(username);
        players.put(username, nullPlayer);
        LOGGER.info("Returning with emitter {}", emitter);
        return emitter;
    }

    public void exit(String username) {
        players.remove(username);
    }

    private void hookupChangeListeners() {
        players.addListener((MapChangeListener.Change<? extends String, ? extends Player> change) -> {
            LOGGER.info("Players map changed: {}", change);
            if (change.wasAdded() && nullPlayer.equals(change.getValueAdded())) {
                Player player = findPlayer(change.getKey());
                LOGGER.info("NullPlayer was added. Changing it to {}", player);
                players.put(change.getKey(), player);
            } else if (change.wasAdded() && !nullPlayer.equals(change.getValueAdded())) {
                LOGGER.info("Storing username {} for player {}", change.getValueAdded(), change.getKey());
                updateUsername(change.getValueAdded(), change.getKey());
                LOGGER.info("Broadcasting new user arrived");
                broadcast(change.getKey());
                LOGGER.info("Sending initial data to {}", change.getValueAdded());
                notifyPlayer(change.getValueAdded());
                LOGGER.info("Starting game if necessary");
                startTheGameIfNecessary();
            } else {
                notificationService.pause();
            }
        });
    }

    private Player findPlayer(String username) {
        Optional<Player> player = findPlayerByUsername(username);
        return player.isPresent() ? player.get() : findFistFreePlayerSlot();
    }

    private Optional<Player> findPlayerByUsername(String username) {
        return gameLogicService.getGameEntity().getPlayers().stream().filter(p -> username.equals(p.getUsername())).findFirst();
    }

    private Player findFistFreePlayerSlot() {
        Optional<Player> player = gameLogicService.getGameEntity().getPlayers().stream().filter(p -> null == p.getUsername() || "".equals(p.getUsername())).findAny();
        if (!player.isPresent()) {
            throw new InvalidPlayerConfiguration("There is no room for this player.");
        }
        return player.get();
    }

    private void updateUsername(Player player, String username) {
        player.setUsername(username);
        playerRepository.save(player);
    }

    private void broadcast(String username) {
        notificationService.broadcast(new PlayerJoinedNotification(username));
    }

    private void notifyPlayer(Player player) {
        PlayerStatusNotification notification = playerConverter.convert(player);
        notification.setAlreadyWaiting(players.keySet());
        notificationService.notify(player.getUsername(), notification);
    }

    private void startTheGameIfNecessary() {
        if (players.size() == gameLogicService.getGameEntity().getPlayers().size()) {
            notificationService.broadcast(gameLogicService.getGameStatusNotification());
        }
    }
}
