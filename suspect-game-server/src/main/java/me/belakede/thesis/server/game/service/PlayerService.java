package me.belakede.thesis.server.game.service;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableMap;
import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.server.game.domain.NullPlayer;
import me.belakede.thesis.server.game.domain.Player;
import me.belakede.thesis.server.game.repository.PlayerRepository;
import me.belakede.thesis.server.game.response.CurrentPlayerNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final GameLogicService gameLogicService;
    private final NotificationService notificationService;
    private final ObservableMap<Figurine, Player> players;
    private final ObjectProperty<Player> currentPlayer;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, GameLogicService gameLogicService, NotificationService notificationService) {
        this.playerRepository = playerRepository;
        this.gameLogicService = gameLogicService;
        this.notificationService = notificationService;
        this.players = new SimpleMapProperty<>();
        this.currentPlayer = new SimpleObjectProperty<>(new NullPlayer());
        hookupChangeListeners();
    }

    public void next() {
        updatePlayers();
        setupNextPlayer();
        updateLogic();
    }

    public Player killPlayer() {
        getCurrentPlayer().setAlive(false);
        return playerRepository.save(getCurrentPlayer());
    }

    public Player getCurrentPlayer() {
        return currentPlayer.get();
    }

    public Player getNextPlayer() {
        return players.get(getNextFigurine());
    }

    private void updatePlayers() {
        if (players.isEmpty()) {
            gameLogicService.getGameEntity().getPlayers().forEach(p -> players.put(p.getFigurine(), p));
        }
    }

    private void setupNextPlayer() {
        currentPlayer.setValue(getNextPlayer());
    }

    private Suspect getNextFigurine() {
        return gameLogicService.getGameLogic().getNextPlayer().getFigurine();
    }

    private void updateLogic() {
        gameLogicService.getGameLogic().next();
    }

    private void hookupChangeListeners() {
        final NullPlayer nullPlayer = new NullPlayer();
        currentPlayer.addListener((observable, oldValue, newValue) -> {
            if (!nullPlayer.equals(newValue)) {
                updateRepository(newValue);
                broadcast(newValue.getUsername());
            }
        });
    }

    private void broadcast(String username) {
        notificationService.broadcast(new CurrentPlayerNotification(username));
    }

    private void updateRepository(Player currentPlayer) {
        players.values().forEach(p -> p.setCurrent(p.equals(currentPlayer)));
        playerRepository.save(players.values());
    }

}
