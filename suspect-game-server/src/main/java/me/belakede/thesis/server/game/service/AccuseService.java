package me.belakede.thesis.server.game.service;

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
    }

    void accuse(Suspicion suspicion) {
        if (!Action.ACCUSE.equals(gameService.getLastAction())) {
            broadcastAccusation(suspicion);
            updateGameLogic(suspicion);
            if (isGameEnded()) {
                broadcastGameIsEnded();
                finishGame();
                resetPlayerService();
                resetNotificationService();
            } else {
                killCurrentPlayer();
            }
            gameService.changeLastAction(Action.ACCUSE);
        }
    }

    private void killCurrentPlayer() {
        LOGGER.debug("Killing current player");
        playerService.kill();
    }

    private void resetNotificationService() {
        notificationService.clear();
    }

    private void resetPlayerService() {
        playerService.finish();
    }

    private void broadcastGameIsEnded() {
        LOGGER.debug("Finishing game");
        notificationService.broadcast(new GameEndedNotification(playerService.getWinner()));
    }

    private void finishGame() {
        gameService.finishTheGame();
    }

    private boolean isGameEnded() {
        return gameService.getGameLogic().isGameEnded();
    }

    private void updateGameLogic(Suspicion newValue) {
        gameService.getGameLogic().accuse(newValue);
    }

    private void broadcastAccusation(Suspicion newValue) {
        LOGGER.debug("Broadcasting accusation: {}", newValue);
        notificationService.broadcast(new AccusationNotification(newValue));
    }

}
