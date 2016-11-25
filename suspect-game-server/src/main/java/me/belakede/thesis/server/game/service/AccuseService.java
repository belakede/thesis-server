package me.belakede.thesis.server.game.service;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import me.belakede.thesis.game.equipment.Suspicion;
import me.belakede.thesis.server.game.domain.Action;
import me.belakede.thesis.server.game.response.AccusationNotification;
import me.belakede.thesis.server.game.response.GameEndedNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class AccuseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccuseService.class);

    private final ObjectProperty<Suspicion> suspicion = new SimpleObjectProperty<>();
    private final GameService gameService;
    private final PlayerService playerService;
    private final PositionService positionService;
    private final NotificationService notificationService;

    @Autowired
    public AccuseService(GameService gameService, PlayerService playerService, PositionService positionService, NotificationService notificationService) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.positionService = positionService;
        this.notificationService = notificationService;
        hookupChangeListeners();
    }

    void accuse(Suspicion suspicion) {
        if (!gameService.getLastAction().equals(Action.ACCUSE)) {
            setSuspicion(suspicion);
            gameService.changeLastAction(Action.ACCUSE);
        }
    }

    private void hookupChangeListeners() {
        suspicionProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.debug("Broadcasting accusation: {}", newValue);
            broadcastAccusation(newValue);
            updateGameLogic(newValue);
            updatePositions();
            if (isGameEnded()) {
                LOGGER.debug("Finishing game");
                broadcastGameIsEnded();
                finishGame();
                resetPlayerService();
                resetNotificationService();
            } else {
                LOGGER.debug("Killing current player");
                killCurrentPlayer();
            }
        });
    }

    private ObjectProperty<Suspicion> suspicionProperty() {
        return suspicion;
    }

    private void setSuspicion(Suspicion suspicion) {
        this.suspicion.set(suspicion);
    }

    private void killCurrentPlayer() {
        playerService.kill();
    }

    private void resetNotificationService() {
        notificationService.clear();
    }

    private void resetPlayerService() {
        playerService.finish();
    }

    private void broadcastGameIsEnded() {
        notificationService.broadcast(new GameEndedNotification(playerService.getWinner()));
    }

    private void finishGame() {
        gameService.finishTheGame();
    }

    private boolean isGameEnded() {
        return gameService.getGameLogic().isGameEnded();
    }

    private void updatePositions() {
        positionService.update();
    }

    private void updateGameLogic(Suspicion newValue) {
        gameService.getGameLogic().accuse(newValue);
    }

    private void broadcastAccusation(Suspicion newValue) {
        notificationService.broadcast(new AccusationNotification(newValue));
    }

}
