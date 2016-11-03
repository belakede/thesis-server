package me.belakede.thesis.server.game.converter;

import me.belakede.thesis.game.equipment.Room;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;
import me.belakede.thesis.server.game.domain.Player;
import me.belakede.thesis.server.game.domain.PlayerCard;
import me.belakede.thesis.server.game.response.PlayerStatusNotification;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class PlayerConverterTest {

    private PlayerConverter testSubject;

    @Before
    public void setUp() throws Exception {
        testSubject = new PlayerConverter(new CardConverter());
    }

    @Test
    public void testConvertShouldCreateAPlayerNotificationFromPlayerEntity() {
        Set<PlayerCard> cards = new HashSet<>();
        cards.add(new PlayerCard(Suspect.PLUM.name()));
        cards.add(new PlayerCard(Room.HALL.name()));
        cards.add(new PlayerCard(Weapon.WRENCH.name()));
        Player player = new Player(Suspect.MUSTARD, cards);
        player.getCards().forEach(c -> c.setPlayer(player));

        PlayerStatusNotification actual = testSubject.convert(player);

        assertThat(actual, notNullValue());
        assertThat(actual.getFigurine(), is(Suspect.MUSTARD));
        assertThat(actual.getCards(), containsInAnyOrder(Suspect.PLUM.name(), Room.HALL.name(), Weapon.WRENCH.name()));

    }
}