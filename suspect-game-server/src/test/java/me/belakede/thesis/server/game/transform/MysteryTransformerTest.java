package me.belakede.thesis.server.game.transform;

import me.belakede.thesis.game.equipment.Case;
import me.belakede.thesis.game.equipment.Room;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;
import me.belakede.thesis.internal.game.equipment.DefaultCase;
import me.belakede.thesis.internal.game.equipment.DefaultSuspicion;
import me.belakede.thesis.server.game.domain.Game;
import me.belakede.thesis.server.game.domain.Mystery;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MysteryTransformerTest {

    private MysteryTransformer testSubject;

    @Before
    public void setUp() throws Exception {
        testSubject = new MysteryTransformer();
    }

    @Test
    public void testTransformShouldCorrectlyTransformFromMysteryToCase() {
        Mystery mystery = new Mystery();
        mystery.setGame(new Game());
        mystery.setSuspect(Suspect.PLUM);
        mystery.setRoom(Room.BILLIARD_ROOM);
        mystery.setWeapon(Weapon.CANDLESTICK);

        Case actual = testSubject.transform(mystery);

        assertThat(actual.getSuspect(), is(Suspect.PLUM));
        assertThat(actual.getRoom(), is(Room.BILLIARD_ROOM));
        assertThat(actual.getWeapon(), is(Weapon.CANDLESTICK));
    }

    @Test
    public void testTransformShouldCorrectlyTransformFromCaseToMystery() {
        Case mystery = new DefaultCase(new DefaultSuspicion(Suspect.PEACOCK, Room.HALL, Weapon.ROPE));

        Mystery actual = testSubject.transform(mystery);

        assertThat(actual.getSuspect(), is(Suspect.PEACOCK));
        assertThat(actual.getRoom(), is(Room.HALL));
        assertThat(actual.getWeapon(), is(Weapon.ROPE));
    }
}