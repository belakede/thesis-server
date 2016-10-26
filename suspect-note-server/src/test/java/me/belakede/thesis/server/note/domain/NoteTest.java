package me.belakede.thesis.server.note.domain;

import me.belakede.junit.ExtendedEqualsHashCodeTestCase;
import me.belakede.thesis.game.equipment.Marker;
import me.belakede.thesis.game.equipment.Weapon;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.Arrays;

@RunWith(Enclosed.class)
public class NoteTest {

    public static final class NoteEqualsAndHashCodeTest extends ExtendedEqualsHashCodeTestCase {

        public NoteEqualsAndHashCodeTest(String name) {
            super(name, Note.class, Arrays.asList("author", "owner", "card"));
        }

        @Override
        protected Note createInstance() throws Exception {
            return new Note(new Author("admin", "test-room"), "demo", Weapon.ROPE, Marker.MAYBE);
        }

        @Override
        protected Note createNotEqualInstance() throws Exception {
            return new Note(new Author("testuser", "test-room"), "demo", Weapon.ROPE, Marker.YES);
        }
    }

}