package me.belakede.thesis.server.game.converter;

import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.internal.game.util.Cards;
import me.belakede.thesis.server.game.domain.PlayerCard;
import org.springframework.stereotype.Component;

@Component
public class CardConverter {

    public Card convert(PlayerCard card) {
        return Cards.valueOf(card.getCard()).get();
    }

    public PlayerCard convert(Card card) {
        return new PlayerCard(card.name());
    }

}
