package me.belakede.thesis.server.note.domain;

import me.belakede.thesis.game.equipment.Marker;
import me.belakede.thesis.game.equipment.Weapon;
import me.belakede.thesis.junit.ExtendedEqualsHashCodeTestCase;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

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

        @Test
        public void testEqualsShouldReturnFalseWhenMarkerIsDifferent() throws Exception {
            Note actualNote = createInstance();
            Note expectedNote = createInstance();
            expectedNote.setMarker(Marker.MAYBE_NOT);
            assertThat(actualNote, is(not(expectedNote)));
        }

        @Test
        public void testHashCodeShouldReturnDifferentValueWhenMarkerIsNull() throws Exception {
            Note actualNote = createInstance();
            Note expectedNote = createInstance();
            expectedNote.setMarker(null);
            assertThat(actualNote.hashCode(), is(not(expectedNote.hashCode())));
        }
    }

}