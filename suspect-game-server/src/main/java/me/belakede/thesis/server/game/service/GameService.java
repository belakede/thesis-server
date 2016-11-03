package me.belakede.thesis.server.game.service;

import javafx.beans.property.SimpleMapProperty;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import me.belakede.thesis.server.game.converter.GameConverter;
import me.belakede.thesis.server.game.converter.PlayerConverter;
import me.belakede.thesis.server.game.domain.Game;
import me.belakede.thesis.server.game.domain.NullPlayer;
import me.belakede.thesis.server.game.domain.Player;
import me.belakede.thesis.server.game.exception.InvalidPlayerConfiguration;
import me.belakede.thesis.server.game.repository.GameRepository;
import me.belakede.thesis.server.game.response.PlayerJoinedNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final GameConverter gameConverter;
    private final PlayerConverter playerConverter;
    private final NotificationService notificationService;
    private final ObservableMap<String, Player> players;
    private Game game;

    @Autowired
    public GameService(GameRepository gameRepository, GameConverter gameConverter, PlayerConverter playerConverter, NotificationService notificationService) {
        this.gameRepository = gameRepository;
        this.gameConverter = gameConverter;
        this.playerConverter = playerConverter;
        this.notificationService = notificationService;
        this.players = new SimpleMapProperty<>();
        hookupChangeListeners();
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean gameInProgress() {
        return game != null;
    }

    public SseEmitter createEmitter(String username) {
        players.put(username, new NullPlayer());
        return notificationService.createEmitter(username);
    }

    private void hookupChangeListeners() {
        players.addListener(new MapChangeListener<String, Player>() {
            @Override
            public void onChanged(Change<? extends String, ? extends Player> change) {
                if (change.wasAdded()) {
                    if (change.getValueAdded().equals(new NullPlayer())) {
                        Optional<Player> player = findPlayerByUsername(change.getKey());
                        players.put(change.getKey(), player.isPresent() ? player.get() : findFistFreePlayerSlot());
                    } else {
                        change.getValueAdded().setUsername(change.getKey());
                        notificationService.broadcast(new PlayerJoinedNotification(change.getKey()));
                        notificationService.notify(change.getKey(), playerConverter.convert(change.getValueAdded()));
                        if (players.size() == game.getPlayers().size()) {
                            notificationService.broadcast(gameConverter.convert(game, players.values()));
                        }
                    }
                }
            }
        });
    }

    private Optional<Player> findPlayerByUsername(String username) {
        return game.getPlayers().stream().filter(p -> username.equals(p.getUsername())).findFirst();
    }

    private Player findFistFreePlayerSlot() {
        Optional<Player> player = game.getPlayers().stream().filter(p -> null == p.getUsername() || "".equals(p.getUsername())).findAny();
        if (!player.isPresent()) {
            throw new InvalidPlayerConfiguration("There is no room for this player.");
        }
        return player.get();
    }


}
