package me.belakede.thesis.server.note.service;

import me.belakede.thesis.game.equipment.Room;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Suspicion;
import me.belakede.thesis.game.equipment.Weapon;
import me.belakede.thesis.internal.game.equipment.DefaultSuspicion;
import me.belakede.thesis.server.note.domain.Author;
import me.belakede.thesis.server.note.domain.Suggestion;
import me.belakede.thesis.server.note.exception.MissingSuggestionException;
import me.belakede.thesis.server.test.note.configuration.SuggestionServiceTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
@Import(SuggestionServiceTestConfiguration.class)
public class SuggestionServiceTest {


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SuggestionService suggestionService;

    @Test(expected = MissingSuggestionException.class)
    public void readShouldThrowMissingSuggestionExceptionWhenAuthorDidNotSuggestAnything() throws MissingSuggestionException {
        Author author = entityManager.persist(new Author("admin", "room"));
        suggestionService.read(author);
    }

    @Test
    public void readShouldReturnWithTheSpecifiedSuggestion() throws MissingSuggestionException {
        Author author = entityManager.persist(new Author("admin", "room"));
        Suggestion expectedSuggestion = entityManager.persist(new Suggestion(author, Suspect.MUSTARD, Room.KITCHEN, Weapon.LEAD_PIPE));

        Suggestion suggestion = suggestionService.read(author);

        assertThat(suggestion, is(expectedSuggestion));
        assertThat(suggestion.getAuthor(), is(expectedSuggestion.getAuthor()));
        assertThat(suggestion.getSuspect(), is(expectedSuggestion.getSuspect()));
        assertThat(suggestion.getRoom(), is(expectedSuggestion.getRoom()));
        assertThat(suggestion.getWeapon(), is(expectedSuggestion.getWeapon()));
    }

    @Test
    public void storeShouldReturnTheStoredSuggestion() {
        Author author = entityManager.persist(new Author("admin", "room"));
        Suspicion suspicion = new DefaultSuspicion(Suspect.MUSTARD, Room.KITCHEN, Weapon.LEAD_PIPE);

        Suggestion suggestion = suggestionService.store(author, suspicion);
        assertThat(suggestion.getAuthor(), is(author));
        assertThat(suggestion.getSuspect(), is(suspicion.getSuspect()));
        assertThat(suggestion.getRoom(), is(suspicion.getRoom()));
        assertThat(suggestion.getWeapon(), is(suspicion.getWeapon()));
    }

    @Test
    public void storeShouldChangeThePreviousSuggestionAndNotCreateANowOne() {
        Author author = entityManager.persist(new Author("admin", "room"));
        Suspicion previousSuspicion = new DefaultSuspicion(Suspect.MUSTARD, Room.KITCHEN, Weapon.LEAD_PIPE);
        Suggestion previousSuggestion = suggestionService.store(author, previousSuspicion);
        Suspicion suspicion = new DefaultSuspicion(Suspect.GREEN, Room.BALLROOM, Weapon.REVOLVER);
        Suggestion suggestion = suggestionService.store(author, suspicion);
        Suggestion storedSuggestion = entityManager.find(Suggestion.class, previousSuggestion.getId());

        assertThat(suggestion, is(storedSuggestion));
        assertThat(suggestion.getSuspect(), is(not(previousSuspicion.getSuspect())));
        assertThat(suggestion.getRoom(), is(not(previousSuspicion.getRoom())));
        assertThat(suggestion.getWeapon(), is(not(previousSuspicion.getWeapon())));
    }

}