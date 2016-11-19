package me.belakede.thesis.server.game.service;

import me.belakede.thesis.server.game.response.HeartBeatNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.TimerTask;

@Service
class HeartbeatService extends TimerTask {

    private final GameService gameService;
    private final NotificationService notificationService;

    @Autowired
    public HeartbeatService(GameService gameService, NotificationService notificationService) {
        this.gameService = gameService;
        this.notificationService = notificationService;
    }

    @Override
    public void run() {
        if (gameService.isRunning()) {
            notificationService.broadcast(new HeartBeatNotification());
        }
    }
}
