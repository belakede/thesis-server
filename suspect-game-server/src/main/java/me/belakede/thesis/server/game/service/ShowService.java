package me.belakede.thesis.server.game.service;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.server.game.domain.Action;
import me.belakede.thesis.server.game.response.CardNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class ShowService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShowService.class);

    private final ListProperty<Card> cards = new SimpleListProperty<>();
    private final GameService gameService;
    private final PlayerService playerService;
    private final NotificationService notificationService;

    @Autowired
    public ShowService(GameService gameService, PlayerService playerService, NotificationService notificationService) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.notificationService = notificationService;
        setCards(FXCollections.observableArrayList());
        hookupChangeListeners();
    }

    void show(Card card) {
        if (!Action.SHOW.equals(gameService.getLastAction())) {
            addCard(card);
            gameService.changeLastAction(Action.SHOW);
        }
    }

    public ObservableList<Card> getCards() {
        return cards.get();
    }

    public void setCards(ObservableList<Card> cards) {
        this.cards.set(cards);
    }

    public ListProperty<Card> cardsProperty() {
        return cards;
    }

    private void addCard(Card card) {
        getCards().add(card);
    }

    private void hookupChangeListeners() {
        cardsProperty().addListener((Change<? extends Card> change) -> {
            LOGGER.debug("Shown cards change: {}", change);
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(this::broadcast);
                }
            }
        });
    }

    private void broadcast(Card card) {
        if (card == null) {
            broadcastNotShowing();
        } else {
            broadcastShowing(card);
        }
    }

    private void broadcastNotShowing() {
        notificationService.broadcast(new CardNotification(false));
    }

    private void broadcastShowing(Card newValue) {
        notificationService.broadcast(new CardNotification(true), playerService.getCurrentPlayer().getUsername(), new CardNotification(newValue.name()));
    }
}
