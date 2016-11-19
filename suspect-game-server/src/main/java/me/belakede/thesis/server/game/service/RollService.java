package me.belakede.thesis.server.game.service;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import me.belakede.thesis.game.equipment.PairOfDice;
import me.belakede.thesis.server.game.response.PairOfDiceNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class RollService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RollService.class);

    private final ObjectProperty<PairOfDice> pairOfDice = new SimpleObjectProperty<>();
    private final GameService gameService;
    private final NotificationService notificationService;

    @Autowired
    public RollService(GameService gameService, NotificationService notificationService) {
        this.gameService = gameService;
        this.notificationService = notificationService;
        hookupChangeListeners();
    }

    void roll() {
        setPairOfDice(gameService.getGameLogic().roll());
    }

    private ObjectProperty<PairOfDice> pairOfDiceProperty() {
        return pairOfDice;
    }

    private void setPairOfDice(PairOfDice pairOfDice) {
        this.pairOfDice.set(pairOfDice);
    }

    private void hookupChangeListeners() {
        pairOfDiceProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.debug("Rolled: {}", newValue);
            notificationService.broadcast(new PairOfDiceNotification(newValue.getFirst(), newValue.getSecond()));
        });
    }
}
