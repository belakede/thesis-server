package me.belakede.thesis.server.game.service;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.server.game.response.CardNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowService {

    private final PlayerService playerService;
    private final NotificationService notificationService;
    private final ObjectProperty<Card> cardObjectProperty;

    @Autowired
    public ShowService(PlayerService playerService, NotificationService notificationService) {
        this.playerService = playerService;
        this.notificationService = notificationService;
        this.cardObjectProperty = new SimpleObjectProperty<>();
        hookupChangeListeners();
    }

    public void show(Card card) {
        cardObjectProperty.setValue(card);
    }

    private void hookupChangeListeners() {
        cardObjectProperty.addListener((observable, oldValue, newValue) -> {
            if (null != newValue) {
                notificationService.broadcast(new CardNotification(true), getUsername(), getSpecialNotification(newValue));
            } else {
                notificationService.broadcast(new CardNotification(false));
            }
        });
    }

    private CardNotification getSpecialNotification(Card newValue) {
        return new CardNotification(newValue.name());
    }

    private String getUsername() {
        return playerService.getCurrentPlayer().getUsername();
    }

}
