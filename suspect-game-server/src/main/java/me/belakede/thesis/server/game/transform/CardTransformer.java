package me.belakede.thesis.server.game.transform;

import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.internal.game.util.Cards;
import me.belakede.thesis.server.game.domain.PlayerCard;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CardTransformer {

    public Set<Card> transform(Collection<PlayerCard> cards) {
        return cards.stream().map(this::transform).collect(Collectors.toSet());
    }

    public Set<PlayerCard> transform(Set<Card> cards) {
        return cards.stream().map(this::transform).collect(Collectors.toSet());
    }

    public Card transform(PlayerCard card) {
        return Cards.valueOf(card.getCard()).get();
    }

    public PlayerCard transform(Card card) {
        PlayerCard playerCard = new PlayerCard();
        playerCard.setCard(card.name());
        return playerCard;
    }

}
