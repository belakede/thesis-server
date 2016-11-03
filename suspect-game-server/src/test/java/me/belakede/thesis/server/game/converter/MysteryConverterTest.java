package me.belakede.thesis.server.game.converter;

import me.belakede.thesis.game.equipment.Case;
import me.belakede.thesis.game.equipment.Room;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;
import me.belakede.thesis.internal.game.equipment.DefaultCase;
import me.belakede.thesis.internal.game.equipment.DefaultSuspicion;
import me.belakede.thesis.server.game.domain.Mystery;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;


public class MysteryConverterTest {

    private MysteryConverter testSubject;

    @Before
    public void setUp() throws Exception {
        testSubject = new MysteryConverter();
    }

    @Test
    public void testConvertShouldCreateANewCaseFromAMystery() {
        Mystery mystery = new Mystery(Suspect.MUSTARD, Room.KITCHEN, Weapon.WRENCH);
        Case actual = testSubject.convert(mystery);
        assertThat(actual, notNullValue());
        assertThat(actual.getSuspect(), is(Suspect.MUSTARD));
        assertThat(actual.getRoom(), is(Room.KITCHEN));
        assertThat(actual.getWeapon(), is(Weapon.WRENCH));
    }

    @Test
    public void testConvertShouldCreateANewMysteryFromACase() {
        Case mystery = new DefaultCase(new DefaultSuspicion(Suspect.PEACOCK, Room.HALL, Weapon.KNIFE));
        Mystery actual = testSubject.convert(mystery);
        assertThat(actual, notNullValue());
        assertThat(actual.getSuspect(), is(Suspect.PEACOCK));
        assertThat(actual.getRoom(), is(Room.HALL));
        assertThat(actual.getWeapon(), is(Weapon.KNIFE));
    }
}