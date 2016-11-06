package me.belakede.thesis.server.game.service;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import me.belakede.thesis.game.equipment.Suspicion;
import me.belakede.thesis.server.game.response.ShowYourCardNotification;
import me.belakede.thesis.server.game.response.SuspicionNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuspectService {

    private final PlayerService playerService;
    private final GameLogicService gameLogicService;
    private final NotificationService notificationService;
    private final ObjectProperty<Suspicion> suspicionObjectProperty;

    @Autowired
    public SuspectService(PlayerService playerService, GameLogicService gameLogicService, NotificationService notificationService) {
        this.playerService = playerService;
        this.gameLogicService = gameLogicService;
        this.notificationService = notificationService;
        this.suspicionObjectProperty = new SimpleObjectProperty<>();
        hookupChangeListeners();
    }

    public void suspect(Suspicion suspicion) {
        // TODO change positions
        suspicionObjectProperty.setValue(suspicion);
    }

    private void hookupChangeListeners() {
        suspicionObjectProperty.addListener((observable, oldValue, newValue) -> {
            if (null != newValue) {
                notificationService.broadcast(new SuspicionNotification(newValue));
                gameLogicService.getGameLogic().suspect(newValue);
                notificationService.notify(playerService.getCurrentPlayer().getUsername(), new ShowYourCardNotification());
            }
        });
    }

}