package me.belakede.thesis.server.note.domain;

import me.belakede.junit.ExtendedEqualsHashCodeTestCase;
import me.belakede.thesis.game.equipment.Room;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.Collections;

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
    }

}