package me.belakede.thesis.server.note.service;

import me.belakede.thesis.game.equipment.Marker;
import me.belakede.thesis.game.equipment.Room;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;
import me.belakede.thesis.server.note.domain.Author;
import me.belakede.thesis.server.note.domain.Note;
import me.belakede.thesis.server.test.note.configuration.NoteServiceTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
@Import(NoteServiceTestConfiguration.class)
public class NoteServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NoteService noteService;

    @Test
    public void createShouldPersistTheSpecifiedNote() {
        Note expectedNote = new Note(new Author("user", "room"), "demo", Weapon.KNIFE, Marker.MAYBE);
        Note note = noteService.create(expectedNote);
        Note persistedNote = entityManager.find(Note.class, note.getId());
        assertThat(note, is(expectedNote));
        assertThat(persistedNote, notNullValue());
        assertThat(note, is(persistedNote));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void createShouldThrowDataIntegrityViolationExceptionWhenEntryAlreadyExists() {
        Author author = entityManager.persist(new Author("user", "room"));
        Note expectedNote = new Note(author, "demo", Weapon.KNIFE, Marker.MAYBE);
        entityManager.persist(expectedNote);
        noteService.create(new Note(author, "demo", Weapon.KNIFE, Marker.MAYBE));
    }

    @Test
    public void findAllByAuthorShouldReturnAnEmptyWhenThereIsNoNotesInDatabase() {
        Author author = entityManager.persist(new Author("user", "room"));
        List<Note> notes = noteService.findAllByAuthor(author);
        assertThat(notes.size(), is(0));
    }

    @Test
    public void findAllByAuthorShouldReturnAllNotesWhichBelongsToTheSpecifiedAuthor() {
        Author author = entityManager.persist(new Author("admin", "room"));
        Author otherAuthor = entityManager.persist(new Author("user", "room"));

        entityManager.persist(new Note(author, "demo", Weapon.KNIFE, Marker.MAYBE));
        entityManager.persist(new Note(author, "demo", Weapon.WRENCH, Marker.YES));
        entityManager.persist(new Note(author, "demo", Suspect.PEACOCK, Marker.NOT));
        entityManager.persist(new Note(otherAuthor, "demo", Weapon.ROPE, Marker.NOT));
        entityManager.persist(new Note(otherAuthor, "demo", Room.BEDROOM, Marker.MAYBE_NOT));

        List<Note> notes = noteService.findAllByAuthor(author);
        assertThat(notes.size(), is(3));
    }


}