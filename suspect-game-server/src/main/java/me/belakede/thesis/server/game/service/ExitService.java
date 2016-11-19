package me.belakede.thesis.server.game.service;

import me.belakede.thesis.server.game.response.GamePausedNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class ExitService {

    private final GameService gameService;
    private final PlayerService playerService;
    private final NotificationService notificationService;

    @Autowired
    public ExitService(GameService gameService, PlayerService playerService, NotificationService notificationService) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.notificationService = notificationService;
    }

    void exit() {
        broadcastGamePaused();
        resetGameService();
        resetNotificationService();
        resetPlayerService();
    }

    private void resetPlayerService() {
        playerService.finish();
    }

    private void resetNotificationService() {
        notificationService.clear();
    }

    private void resetGameService() {
        gameService.pauseTheGame();
    }

    private void broadcastGamePaused() {
        notificationService.broadcast(new GamePausedNotification());
    }
}
