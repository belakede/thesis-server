package me.belakede.thesis.server.chat.service;

import me.belakede.thesis.server.chat.domain.Message;
import me.belakede.thesis.server.chat.domain.Sender;
import me.belakede.thesis.server.chat.exception.MissingSenderException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MessageServiceTest {

    private MessageService testSubject;

    @Before
    public void setUp() throws Exception {
        testSubject = new MessageService();

    }

    @Test
    public void createShouldReturnAValidMessageWithId() throws Exception {
        Sender sender = new Sender("testuser", "testroom");
        Message message = testSubject.create(sender, "Test message");
        assertThat(message.getSender(), is("testuser"));
        assertThat(message.getMessage(), is("Test message"));
    }

    @Test(expected = MissingSenderException.class)
    public void createShouldThrowMissingSenderExceptionWhenSenderIsNull() throws MissingSenderException {
        testSubject.create(null, "Test message");
    }

}