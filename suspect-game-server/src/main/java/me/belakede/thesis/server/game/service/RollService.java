package me.belakede.thesis.server.game.service;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import me.belakede.thesis.game.equipment.PairOfDice;
import me.belakede.thesis.server.game.response.PairOfDiceNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RollService {

    private final GameLogicService gameLogicService;
    private final NotificationService notificationService;
    private final ObjectProperty<PairOfDice> pairOfDiceObjectProperty;

    @Autowired
    public RollService(GameLogicService gameLogicService, NotificationService notificationService) {
        this.gameLogicService = gameLogicService;
        this.notificationService = notificationService;
        this.pairOfDiceObjectProperty = new SimpleObjectProperty<>();
        hookupChangeListeners();
    }

    public void roll() {
        pairOfDiceObjectProperty.setValue(gameLogicService.getGameLogic().roll());
    }

    private void hookupChangeListeners() {
        pairOfDiceObjectProperty.addListener((observable, oldValue, newValue) -> {
            if (null != newValue) {
                notificationService.broadcast(new PairOfDiceNotification(newValue.getFirst(), newValue.getSecond()));
            }
        });
    }

}
