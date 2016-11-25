package me.belakede.thesis.server.game.service;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import me.belakede.thesis.game.equipment.PairOfDice;
import me.belakede.thesis.server.game.domain.Action;
import me.belakede.thesis.server.game.response.PairOfDiceNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class RollService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RollService.class);

    private final ListProperty<PairOfDice> pairOfDice = new SimpleListProperty<>();
    private final GameService gameService;
    private final NotificationService notificationService;

    @Autowired
    public RollService(GameService gameService, NotificationService notificationService) {
        this.gameService = gameService;
        this.notificationService = notificationService;
        setPairOfDice(FXCollections.observableArrayList());
        hookupChangeListeners();
    }

    void roll() {
        addPairOfDice(gameService.getGameLogic().roll());
        gameService.changeLastAction(Action.ROLL);
    }

    public ObservableList<PairOfDice> getPairOfDice() {
        return pairOfDice.get();
    }

    private void setPairOfDice(ObservableList<PairOfDice> pairOfDice) {
        this.pairOfDice.set(pairOfDice);
    }

    private ListProperty<PairOfDice> pairOfDiceProperty() {
        return pairOfDice;
    }

    private void addPairOfDice(PairOfDice pairOfDice) {
        getPairOfDice().add(pairOfDice);
    }

    private void hookupChangeListeners() {
        pairOfDiceProperty().addListener((Change<? extends PairOfDice> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(newValue -> {
                        LOGGER.debug("Rolled: {}", newValue);
                        notificationService.broadcast(new PairOfDiceNotification(newValue.getFirst(), newValue.getSecond()));
                    });
                }
            }
        });
    }
}
