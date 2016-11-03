package me.belakede.thesis.server.game.service;

import me.belakede.thesis.server.game.response.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);
    private final Map<String, SseEmitter> emitters;

    public NotificationService() {
        emitters = new ConcurrentHashMap<>();
    }

    public SseEmitter createEmitter(String username) {
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
            if (!notify(entry.getKey(), message)) {
                missing.add(entry.getKey());
            }
        });
        missing.forEach(emitters::remove);
    }

    public boolean notify(String user, Notification notification) {
        boolean sent = false;
        if (emitters.containsKey(user)) {
            try {
                emitters.get(user).send(notification, MediaType.APPLICATION_JSON);
                sent = true;
            } catch (IOException exception) {
                emitters.get(user).complete();
                LOGGER.warn("User {} is not available: {}", user, exception);
            }
        }
        return sent;
    }

    public void close() {
        emitters.values().forEach(ResponseBodyEmitter::complete);
        emitters.clear();
    }


}
