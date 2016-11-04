package me.belakede.thesis.server.game.service;

import javafx.beans.property.SimpleMapProperty;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import me.belakede.thesis.server.game.converter.PlayerConverter;
import me.belakede.thesis.server.game.domain.NullPlayer;
import me.belakede.thesis.server.game.domain.Player;
import me.belakede.thesis.server.game.exception.InvalidPlayerConfiguration;
import me.belakede.thesis.server.game.repository.PlayerRepository;
import me.belakede.thesis.server.game.response.PlayerJoinedNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JoinService {

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
        this.players = new SimpleMapProperty<>();
        this.nullPlayer = new NullPlayer();
        hookupChangeListeners();
    }

    public void add(String username) {
        players.put(username, nullPlayer);
    }

    public void remove(String username) {
        players.remove(username);
    }

    private void hookupChangeListeners() {
        players.addListener((MapChangeListener.Change<? extends String, ? extends Player> change) -> {
            if (change.wasAdded() && nullPlayer.equals(change.getValueAdded())) {
                players.put(change.getKey(), findPlayer(change.getKey()));
            } else if (change.wasAdded() && !nullPlayer.equals(change.getValueAdded())) {
                updateUsername(change.getValueAdded(), change.getKey());
                broadcast(change.getKey());
                notifyPlayer(change.getValueAdded());
                startTheGameIfNecessary();
            } else {
                // TODO stop the game and store the current state when any user leaves the game
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
        notificationService.notify(player.getUsername(), playerConverter.convert(player));
    }

    private void startTheGameIfNecessary() {
        if (players.size() == gameLogicService.getGameEntity().getPlayers().size()) {
            notificationService.broadcast(gameLogicService.getGameStatusNotification());
        }
    }
}
