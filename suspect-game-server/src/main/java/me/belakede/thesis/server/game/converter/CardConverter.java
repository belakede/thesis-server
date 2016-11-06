package me.belakede.thesis.server.game.converter;

import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.internal.game.util.Cards;
import me.belakede.thesis.server.game.domain.PlayerCard;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class CardConverter {

    public Card convert(PlayerCard card) {
        return Cards.valueOf(card.getCard()).get();
    }

    public PlayerCard convert(Card card) {
        return new PlayerCard(card.name());
    }

    public Collection<String> convert(Collection<PlayerCard> playerCards) {
        return playerCards.stream().map(this::convert).map(c -> c.name()).collect(Collectors.toSet());
    }

}
