package me.belakede.thesis.server.game.service;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
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

    private final ListProperty<Suspicion> suspicion = new SimpleListProperty<>();
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
        setSuspicion(FXCollections.observableArrayList());
        hookupChangeListeners();
    }

    void suspect(Suspicion suspicion) {
        if (!Action.SUSPECT.equals(gameService.getLastAction())) {
            addSuspicion(suspicion);
            gameService.changeLastAction(Action.SUSPECT);
        }
    }

    private void hookupChangeListeners() {
        suspicionProperty().addListener((Change<? extends Suspicion> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(newValue -> {
                        LOGGER.debug("Broadcasting suspicion: {}");
                        broadcastSuspicion(newValue);
                        updateGameLogic(newValue);
                        updatePositions();
                        LOGGER.debug("Sending show your card notification to next player.");
                        sendShowYourCardNotification();
                    });
                }
            }
            suspicionProperty().clear();
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

    public ObservableList<Suspicion> getSuspicion() {
        return suspicion.get();
    }

    private void setSuspicion(ObservableList<Suspicion> suspicion) {
        this.suspicion.set(suspicion);
    }

    private ListProperty<Suspicion> suspicionProperty() {
        return suspicion;
    }

    private void addSuspicion(Suspicion suspicion) {
        getSuspicion().add(suspicion);
    }
}
