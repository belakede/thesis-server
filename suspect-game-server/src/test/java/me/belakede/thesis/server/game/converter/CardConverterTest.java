package me.belakede.thesis.server.game.converter;

import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Room;
import me.belakede.thesis.game.equipment.Weapon;
import me.belakede.thesis.server.game.domain.Player;
import me.belakede.thesis.server.game.domain.PlayerCard;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class CardConverterTest {

    private CardConverter testSubject;

    @Before
    public void setUp() throws Exception {
        testSubject = new CardConverter();
    }

    @Test
    public void testConvertShouldReturnACardBasedOnPlayerCard() throws Exception {
        Player player = Mockito.mock(Player.class);
        PlayerCard playerCard = new PlayerCard(player, Room.LIBRARY.name());
        Card actual = testSubject.convert(playerCard);
        assertThat(actual, notNullValue());
        assertThat(actual, is(Room.LIBRARY));
    }

    @Test
    public void testConvertShouldReturnAPlayerCardForTheSpecifiedPlayerFromACard() throws Exception {
        PlayerCard actual = testSubject.convert(Weapon.ROPE);
        assertThat(actual, notNullValue());
        assertThat(actual.getCard(), is(Weapon.ROPE.name()));
    }

    @Test
    public void testConvertShouldReturnACollectionOfString() {
        Player player = Mockito.mock(Player.class);
        List<PlayerCard> playerCards = Arrays.asList(new PlayerCard(player, Room.BALLROOM.name()), new PlayerCard(player, Weapon.KNIFE.name()));
        Collection<String> actual = testSubject.convert(playerCards);
        assertThat(actual.size(), is(2));
        assertThat(actual, containsInAnyOrder("BALLROOM", "KNIFE"));
    }
}