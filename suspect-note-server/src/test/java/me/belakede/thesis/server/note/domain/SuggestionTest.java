package me.belakede.thesis.server.note.domain;

import me.belakede.junit.ExtendedEqualsHashCodeTestCase;
import me.belakede.thesis.game.equipment.Room;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(Enclosed.class)
public class SuggestionTest {

    public static final class SuggestionEqualsHashCodeTestCase extends ExtendedEqualsHashCodeTestCase {

        public SuggestionEqualsHashCodeTestCase(String name) {
            super(name, Suggestion.class, Collections.singletonList("author"));
        }

        @Override
        protected Suggestion createInstance() throws Exception {
            return new Suggestion(new Author("admin", "test-room"), Suspect.MUSTARD, Room.BEDROOM, Weapon.CANDLESTICK);
        }

        @Override
        protected Suggestion createNotEqualInstance() throws Exception {
            return new Suggestion(new Author("demo", "test-room"), Suspect.MUSTARD, Room.BEDROOM, Weapon.CANDLESTICK);
        }

        @Test
        public void testEqualsShouldReturnFalseWhenSuspectIsDifferent() throws Exception {
            Suggestion expectedSuggestion = createInstance();
            Suggestion actualSuggestion = createInstance();
            actualSuggestion.setSuspect(Suspect.GREEN);
            assertThat(actualSuggestion, is(not(expectedSuggestion)));
        }

        @Test
        public void testEqualsShouldReturnFalseWhenRoomIsDifferent() throws Exception {
            Suggestion expectedSuggestion = createInstance();
            Suggestion actualSuggestion = createInstance();
            actualSuggestion.setRoom(Room.DINING_ROOM);
            assertThat(actualSuggestion, is(not(expectedSuggestion)));
        }

        @Test
        public void testEqualsShouldReturnFalseWhenWeaponIsDifferent() throws Exception {
            Suggestion expectedSuggestion = createInstance();
            Suggestion actualSuggestion = createInstance();
            actualSuggestion.setWeapon(Weapon.WRENCH);
            assertThat(actualSuggestion, is(not(expectedSuggestion)));
        }
    }

}