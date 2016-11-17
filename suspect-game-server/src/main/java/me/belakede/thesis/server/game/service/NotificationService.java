package me.belakede.thesis.server.game.service;

import me.belakede.thesis.server.game.response.GamePausedNotification;
import me.belakede.thesis.server.game.response.HeartBeatNotification;
import me.belakede.thesis.server.game.response.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    private final GameLogicService gameLogicService;
    private final Map<String, SseEmitter> emitters;

    @Autowired
    public NotificationService(GameLogicService gameLogicService) {
        this.gameLogicService = gameLogicService;
        this.emitters = new ConcurrentHashMap<>();
        startHealthCheck();
    }

    public SseEmitter createEmitter(String username) {
        LOGGER.info("Creating emitter for {}", username);
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(username, emitter);
        return emitter;
    }

    public void broadcast(Notification notification) {
        broadcast(notification, new HashMap<>());
    }

    public void broadcast(Notification notification, String user, Notification specialNotification) {
        HashMap<String, Notification> specialNotifications = new HashMap<>(1);
        specialNotifications.put(user, specialNotification);
        broadcast(notification, specialNotifications);
    }

    public void broadcast(Notification notification, Map<String, Notification> specialNotifications) {
        List<String> missing = new ArrayList<>();
        emitters.entrySet().forEach(entry -> {
            Notification message = specialNotifications.containsKey(entry.getKey()) ? specialNotifications.get(entry.getKey()) : notification;
            if (!notifyPlayer(entry.getKey(), message)) {
                missing.add(entry.getKey());
            }
        });
        LOGGER.info("Missing players: ", missing);
        if (!missing.isEmpty()) {
            LOGGER.info("Pausing game.");
            pause();
        }
    }

    public void notify(String user, Notification notification) {
        if (!notifyPlayer(user, notification)) {
            LOGGER.info("Can't notify player. Pausing game!");
            pause();
        }
    }

    public void pause() {
        pauseGame();
        close();
    }

    public void close() {
        LOGGER.info("Closing emitters...");
        emitters.values().forEach(ResponseBodyEmitter::complete);
        emitters.clear();
    }

    private void startHealthCheck() {
        Thread thread = new Thread(() -> {
            while (true) {
                int sleepTime = 30 * 1000;
                if (gameLogicService.gameInProgress() && !emitters.isEmpty()) {
                    LOGGER.info("Sending heartbeat...");
                    broadcast(new HeartBeatNotification());
                    sleepTime = 10 * 1000;
                }
                LOGGER.info("Waiting {} seconds before next", sleepTime / 1000);
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    LOGGER.info("Interrupted sleep", e);
                }
            }
        });
        thread.setName("heartbeat");
        thread.setDaemon(true);
        thread.start();
    }

    private void pauseGame() {
        gameLogicService.pauseTheGame();
        emitters.entrySet().forEach(es -> notifyPlayer(es.getKey(), new GamePausedNotification()));
    }

    private boolean notifyPlayer(String user, Notification notification) {
        boolean sent = false;
        if (emitters.containsKey(user)) {
            try {
                SseEmitter emitter = emitters.get(user);
                LOGGER.info("Notification for {} on channel {}: {}", user, emitter, notification);
                emitter.send(notification, MediaType.APPLICATION_JSON);
                sent = true;
            } catch (Exception exception) {
                LOGGER.warn("User {} is not available: {}", user, exception);
            }
        }
        return sent;
    }

}
