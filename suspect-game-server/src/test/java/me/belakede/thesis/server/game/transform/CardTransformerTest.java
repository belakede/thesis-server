package me.belakede.thesis.server.game.transform;

import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Room;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;
import me.belakede.thesis.server.game.domain.PlayerCard;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isOneOf;


public class CardTransformerTest {

    private CardTransformer testSubject;

    @Before
    public void setUp() throws Exception {
        testSubject = new CardTransformer();
    }

    @Test
    public void testTransformShouldTransformPlayerCardToCard() {
        PlayerCard playerCard = new PlayerCard();
        playerCard.setCard(Room.DINING_ROOM.name());
        Card actual = testSubject.transform(playerCard);
        assertThat(actual, is(Room.DINING_ROOM));
    }

    @Test
    public void testTransformShouldTransfromCardToPlayerCard() {
        PlayerCard actual = testSubject.transform(Suspect.GREEN);
        assertThat(actual.getCard(), is(Suspect.GREEN.name()));
    }

    @Test
    public void testTransformShouldTransformASetOfPlayerCardIntoASetOfCard() {
        Set<PlayerCard> cards = new HashSet<>();
        cards.add(createFrom(Room.BEDROOM));
        cards.add(createFrom(Suspect.WHITE));
        cards.add(createFrom(Weapon.KNIFE));
        cards.add(createFrom(Room.KITCHEN));

        Collection<Card> actual = testSubject.transform(cards);

        assertThat(actual.size(), is(4));
    }

    @Test
    public void testTransformShouldTransformASetOfCardIntoASetOfPlayerCard() {
        Set<Card> cards = new HashSet<>();
        cards.addAll(Arrays.asList(Room.HALL, Room.LIBRARY, Weapon.LEAD_PIPE));

        Set<PlayerCard> actual = testSubject.transform(cards);

        assertThat(actual.size(), is(3));
        assertThat(actual.stream().findAny().get().getCard(), isOneOf(Room.HALL.name(), Room.LIBRARY.name(), Weapon.LEAD_PIPE.name()));
    }

    private PlayerCard createFrom(Card card) {
        PlayerCard playerCard = new PlayerCard();
        playerCard.setCard(card.name());
        return playerCard;
    }
}