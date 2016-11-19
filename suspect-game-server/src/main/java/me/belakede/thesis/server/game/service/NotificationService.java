package me.belakede.thesis.server.game.service;

import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import me.belakede.thesis.server.game.response.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
class NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    private final MapProperty<String, SseEmitter> emitters = new SimpleMapProperty<>();
    private final ListProperty<String> missingUsers = new SimpleListProperty<>();

    public NotificationService() {
        setEmitters(FXCollections.observableMap(new ConcurrentHashMap<>(6)));
        setMissingUsers(FXCollections.observableArrayList());
        hookupChangeListeners();
    }

    SseEmitter openChannelForUser(String username) {
        getEmitters().put(username, new SseEmitter(Long.MAX_VALUE));
        return getEmitters().get(username);
    }

    void broadcast(Notification notification) {
        broadcast(notification, new HashMap<>());
    }

    void broadcast(Notification notification, String user, Notification specialNotification) {
        HashMap<String, Notification> specialNotifications = new HashMap<>(1);
        specialNotifications.put(user, specialNotification);
        broadcast(notification, specialNotifications);
    }

    void notify(String user, Notification notification) {
        if (!notifyPlayer(user, notification)) {
            getMissingUsers().add(user);
        }
    }

    void clear() {
        getEmitters().values().forEach(ResponseBodyEmitter::complete);
        getEmitters().clear();
        missingUsersProperty().clear();
    }

    ObservableList<String> getMissingUsers() {
        return missingUsers.get();
    }

    private void setMissingUsers(ObservableList<String> missingUsers) {
        this.missingUsers.set(missingUsers);
    }

    ListProperty<String> missingUsersProperty() {
        return missingUsers;
    }

    private void hookupChangeListeners() {
        missingUsers.addListener((Change<? extends String> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(user -> {
                        SseEmitter emitter = emitters.remove(user);
                        if (emitter != null) {
                            emitter.complete();
                        }
                    });
                }
            }
        });
    }

    private void broadcast(Notification notification, Map<String, Notification> specialNotifications) {
        emitters.entrySet().forEach(entry -> notify(entry.getKey(), specialNotifications.containsKey(entry.getKey()) ? specialNotifications.get(entry.getKey()) : notification));
    }

    private ObservableMap<String, SseEmitter> getEmitters() {
        return emitters.get();
    }

    private void setEmitters(ObservableMap<String, SseEmitter> emitters) {
        this.emitters.set(emitters);
    }

    private boolean notifyPlayer(String user, Notification notification) {
        boolean sent = false;
        if (emitters.containsKey(user)) {
            try {
                SseEmitter emitter = emitters.get(user);
                LOGGER.info("Notification for {}: {}", user, notification);
                emitter.send(notification, MediaType.APPLICATION_JSON);
                sent = true;
            } catch (Exception exception) {
                LOGGER.warn("User {} is not available.", user);
            }
        }
        return sent;
    }
}
