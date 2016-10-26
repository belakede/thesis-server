package me.belakede.thesis.server.note.service;

import me.belakede.thesis.server.note.domain.Author;
import me.belakede.thesis.server.note.exception.MissingAuthorException;
import me.belakede.thesis.server.note.repository.AuthorRepository;
import me.belakede.thesis.server.test.note.configuration.AuthorServiceTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
@Import(AuthorServiceTestConfiguration.class)
public class AuthorServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuthorRepository repository;

    @Autowired
    private AuthorService authorService;

    @Test
    public void createShouldPersistTheSpecifiedAuthor() {
        Author expectedAuthor = new Author("user", "room");
        Author author = authorService.create("user", "room");
        Author persistedAuthor = entityManager.find(Author.class, author.getId());
        assertThat(author, is(expectedAuthor));
        assertThat(persistedAuthor, notNullValue());
        assertThat(author, is(persistedAuthor));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void createShouldThrowDataIntegrityViolationExceptionWhenAuthorIsNull() {
        authorService.create(null, "room");
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void createShouldThrowDataIntegrityViolationExceptionWhenRoomIsNull() {
        authorService.create("user", null);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void createShouldThrowDataIntegrityViolationExceptionWhenEntryAlreadyExistsWithTheSameAuthorAndRoom() {
        entityManager.persist(new Author("user", "room"));
        authorService.create("user", "room");
    }

    @Test(expected = MissingAuthorException.class)
    public void findByNameAndRoomShouldThrowMissingAuthorExceptionWhenAuthorNotExistsInDatabase() throws MissingAuthorException {
        authorService.findByNameAndRoom("user", "room");
    }

    @Test(expected = MissingAuthorException.class)
    public void findByNameAndRoomShouldThrowMissingAuthorExceptionWhenAuthorIsNull() throws MissingAuthorException {
        authorService.findByNameAndRoom(null, "room");
    }

    @Test(expected = MissingAuthorException.class)
    public void findByNameAndRoomShouldThrowMissingAuthorExceptionWhenRoomIsNull() throws MissingAuthorException {
        authorService.findByNameAndRoom("user", null);
    }

    @Test
    public void findByNameAndRoomShouldReturnWithTheStoredAuthorWhenItsExistsInDatabase() throws MissingAuthorException {
        Author expectedAuthor = new Author("user", "room");
        entityManager.persist(expectedAuthor);
        Author actualAuthor = authorService.findByNameAndRoom("user", "room");
        assertThat(actualAuthor, is(expectedAuthor));
    }

}