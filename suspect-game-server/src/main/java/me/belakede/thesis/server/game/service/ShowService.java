package me.belakede.thesis.server.game.service;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.server.game.response.CardNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class ShowService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShowService.class);

    private final ObjectProperty<Card> card = new SimpleObjectProperty<>();
    private final PlayerService playerService;
    private final NotificationService notificationService;

    @Autowired
    public ShowService(PlayerService playerService, NotificationService notificationService) {
        this.playerService = playerService;
        this.notificationService = notificationService;
        hookupChangeListeners();
    }

    void show(Card card) {
        setCard(card);
    }

    private ObjectProperty<Card> cardProperty() {
        return card;
    }

    private void setCard(Card card) {
        this.card.set(card);
    }

    private void hookupChangeListeners() {
        cardProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.debug("Shown card: {}", newValue);
            if (null != newValue) {
                broadcastShowing(newValue);
            } else {
                broadcastNotShowing();
            }
        });
    }

    private void broadcastNotShowing() {
        notificationService.broadcast(new CardNotification(false));
    }

    private void broadcastShowing(Card newValue) {
        notificationService.broadcast(new CardNotification(true), playerService.getCurrentPlayer().getUsername(), new CardNotification(newValue.name()));
    }
}
