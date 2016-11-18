package me.belakede.thesis.server.chat.service;

import me.belakede.thesis.server.chat.domain.Sender;
import me.belakede.thesis.server.chat.exception.MissingSenderException;
import me.belakede.thesis.server.chat.repository.SenderRepository;
import me.belakede.thesis.test.chat.configuration.SenderServiceTestConfiguration;
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
@Import(SenderServiceTestConfiguration.class)
public class SenderServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SenderService senderService;

    @Autowired
    private SenderRepository repository;

    @Test
    public void createShouldPersistTheSpecifiedSender() {
        Sender expectedSender = new Sender("user", "room");
        Sender sender = senderService.create("user", "room");
        Sender persistedSender = entityManager.find(Sender.class, sender.getId());
        assertThat(sender, is(expectedSender));
        assertThat(persistedSender, notNullValue());
        assertThat(sender, is(persistedSender));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void createShouldThrowDataIntegrityViolationExceptionWhenSenderIsNull() {
        senderService.create(null, "room");
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void createShouldThrowDataIntegrityViolationExceptionWhenRoomIsNull() {
        senderService.create("user", null);
    }

    @Test
    public void createShouldReturnWithAlreadyExistingInTheSameSenderAndRoom() {
        Sender expected = new Sender("user", "room");
        entityManager.persist(expected);
        Sender actual = senderService.create("user", "room");
        assertThat(actual, is(expected));
    }

    @Test(expected = MissingSenderException.class)
    public void findByNameAndRoomShouldThrowMissingSenderExceptionWhenSenderNotExistsInDatabase() throws MissingSenderException {
        senderService.findByNameAndRoom("user", "room");
    }

    @Test(expected = MissingSenderException.class)
    public void findByNameAndRoomShouldThrowMissingSenderExceptionWhenSenderIsNull() throws MissingSenderException {
        senderService.findByNameAndRoom(null, "room");
    }

    @Test(expected = MissingSenderException.class)
    public void findByNameAndRoomShouldThrowMissingSenderExceptionWhenRoomIsNull() throws MissingSenderException {
        senderService.findByNameAndRoom("user", null);
    }

    @Test
    public void findByNameAndRoomShouldReturnWithTheStoredSenderWhenItsExistsInDatabase() throws MissingSenderException {
        Sender expectedSender = new Sender("user", "room");
        entityManager.persist(expectedSender);
        Sender actualSender = senderService.findByNameAndRoom("user", "room");
        assertThat(actualSender, is(expectedSender));
    }

    @Test
    public void deleteByRoomShouldRemoveAllSenderInRoom() {
        repository.save(new Sender("admin", "room"));
        repository.save(new Sender("user", "room"));
        repository.save(new Sender("demo", "room"));

        senderService.deleteByRoom("room");

        List<Sender> senders = repository.findByRoom("room");

        assertThat(senders.size(), is(0));
    }

}