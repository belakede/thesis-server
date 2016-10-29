package me.belakede.thesis.server.game.transform;

import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Room;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;
import me.belakede.thesis.internal.game.DefaultPlayer;
import me.belakede.thesis.server.game.domain.Player;
import me.belakede.thesis.server.game.domain.PlayerCard;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class PlayerTransformerTest {

    private PlayerTransformer testSubject;

    @Before
    public void setUp() throws Exception {
        testSubject = new PlayerTransformer(new CardTransformer());
    }

    @Test
    public void testTransformShouldTransformPlayerEntityToPlayer() {
        Set<PlayerCard> cards = new HashSet<>();
        cards.add(createFrom(Room.BEDROOM));
        cards.add(createFrom(Suspect.WHITE));
        cards.add(createFrom(Weapon.KNIFE));
        cards.add(createFrom(Room.KITCHEN));

        Player player = new Player();
        player.setSuspect(Suspect.PEACOCK);
        player.setPlayerCards(cards);

        me.belakede.thesis.game.Player actual = testSubject.transform(player);
        assertThat(actual.getFigurine(), is(Suspect.PEACOCK));
        assertTrue(actual.hasCard(Room.BEDROOM));
        assertTrue(actual.hasCard(Room.KITCHEN));
        assertTrue(actual.hasCard(Weapon.KNIFE));
    }

    @Test
    public void testTransformShouldTransformPlayerToPlayerEntity() {
        Set<Card> cards = new HashSet<>();
        cards.addAll(Arrays.asList(Room.HALL, Room.LIBRARY, Weapon.LEAD_PIPE));
        me.belakede.thesis.game.Player player = new DefaultPlayer(Suspect.MUSTARD, cards);

        Player actual = testSubject.transform(player);
        assertThat(actual.getSuspect(), is(Suspect.MUSTARD));
        assertThat(actual.getPlayerCards().size(), is(3));
    }

    @Test
    public void testTransformShouldTransformASetOfPlayerEntityToASetOfPlayer() {
        Set<PlayerCard> cards = new HashSet<>();
        cards.add(createFrom(Room.BEDROOM));
        cards.add(createFrom(Suspect.WHITE));
        Set<PlayerCard> otherCards = new HashSet<>();
        otherCards.add(createFrom(Weapon.KNIFE));
        otherCards.add(createFrom(Room.KITCHEN));

        Player player = new Player();
        player.setSuspect(Suspect.PEACOCK);
        player.setPlayerCards(cards);

        Player otherPlayer = new Player();
        otherPlayer.setSuspect(Suspect.MUSTARD);
        otherPlayer.setPlayerCards(otherCards);

        Set<Player> players = new HashSet<>(Arrays.asList(player, otherPlayer));
        Set<me.belakede.thesis.game.Player> actual = testSubject.transform(players);


        assertThat(actual.size(), is(2));
    }

    @Test
    public void testTransformShouldTransformASetOfPlayerToASetOfPlayerEntity() {
        Set<Card> cards = new HashSet<>(Arrays.asList(Room.HALL, Room.LIBRARY, Weapon.LEAD_PIPE));
        me.belakede.thesis.game.Player player = new DefaultPlayer(Suspect.MUSTARD, cards);
        Set<Card> otherCards = new HashSet<>(Arrays.asList(Weapon.LEAD_PIPE, Suspect.GREEN));
        me.belakede.thesis.game.Player otherPlayer = new DefaultPlayer(Suspect.SCARLET, otherCards);

        Set<me.belakede.thesis.game.Player> players = new HashSet<>(Arrays.asList(player, otherPlayer));
        Set<Player> actual = testSubject.transform(players);

        assertThat(actual.size(), is(2));
    }

    private PlayerCard createFrom(Card card) {
        PlayerCard playerCard = new PlayerCard();
        playerCard.setCard(card.name());
        return playerCard;
    }

}