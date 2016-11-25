package me.belakede.thesis.server.game.service;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import me.belakede.thesis.game.equipment.Suspicion;
import me.belakede.thesis.server.game.domain.Action;
import me.belakede.thesis.server.game.response.ShowYourCardNotification;
import me.belakede.thesis.server.game.response.SuspicionNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class SuspectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuspectService.class);

    private final ObjectProperty<Suspicion> suspicion = new SimpleObjectProperty<>();
    private final GameService gameService;
    private final PlayerService playerService;
    private final PositionService positionService;
    private final NotificationService notificationService;

    @Autowired
    public SuspectService(GameService gameService, PlayerService playerService, PositionService positionService, NotificationService notificationService) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.positionService = positionService;
        this.notificationService = notificationService;
        hookupChangeListeners();
    }

    void suspect(Suspicion suspicion) {
        if (!Action.SUSPECT.equals(gameService.getLastAction())) {
            setSuspicion(suspicion);
            gameService.changeLastAction(Action.SUSPECT);
        }
    }

    private void hookupChangeListeners() {
        suspicionProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.debug("Broadcasting suspicion: {}");
            broadcastSuspicion(newValue);
            updateGameLogic(newValue);
            updatePositions();
            LOGGER.debug("Sending show your card notification to next player.");
            sendShowYourCardNotification();
        });
    }

    private void sendShowYourCardNotification() {
        notificationService.notify(playerService.getNextPlayer().getUsername(), new ShowYourCardNotification());
    }

    private void updatePositions() {
        positionService.update();
    }

    private void broadcastSuspicion(Suspicion newValue) {
        notificationService.broadcast(new SuspicionNotification(newValue));
    }

    private void updateGameLogic(Suspicion newValue) {
        gameService.getGameLogic().suspect(newValue);
    }

    private ObjectProperty<Suspicion> suspicionProperty() {
        return suspicion;
    }

    private void setSuspicion(Suspicion suspicion) {
        this.suspicion.set(suspicion);
    }
}
