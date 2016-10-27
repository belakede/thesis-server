package me.belakede.thesis.server.chat.controller;

import me.belakede.jackson.JacksonContextResolver;
import me.belakede.junit.OrderedSpringRunner;
import me.belakede.test.security.oauth2.OAuth2Helper;
import me.belakede.thesis.server.auth.domain.Role;
import me.belakede.thesis.server.chat.domain.Message;
import me.belakede.thesis.server.chat.request.ChatRequest;
import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;


@Import(OAuth2Helper.class)
@RunWith(OrderedSpringRunner.class)
@ComponentScan("me.belakede.thesis.server.auth")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatControllerIntegrationTest {

    private static final String TEST_ROOM_ID = "test-room-id";
    @Value("${suspect.server.admin.username}")
    private String adminUsername;
    @LocalServerPort
    private int randomServerPort;
    @Autowired
    private OAuth2Helper authHelper;

    @Test
    public void test() {
        OAuth2AccessToken accessToken = authHelper.createOAuth2AccessToken("admin", Role.ADMIN.getAuthority());
        createUser(accessToken, "testuser1");
        OAuth2AccessToken testUserAccessToken = authHelper.createOAuth2AccessToken("testuser1", Role.USER.getAuthority());

        List<Thread> threads = Arrays.asList(
                new Thread(new ChatStreamOpener(accessToken)), new Thread(new ChatStreamOpener(testUserAccessToken)),
                new Thread(new ChatMessageSender(accessToken, 1500)), new Thread(new ChatMessageSender(testUserAccessToken, 2000)),
                new Thread(new ChatStreamCloser(accessToken, 5000))
        );
        threads.forEach(Thread::start);
        threads.forEach((thread) -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void createUser(OAuth2AccessToken accessToken, String username) {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/users").queryParam("username", username).queryParam("password", "password");
        webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .post(Entity.entity(null, "text/plain"));
    }

    private final class ChatStreamOpener implements Runnable {

        private final OAuth2AccessToken accessToken;

        public ChatStreamOpener(OAuth2AccessToken accessToken) {
            this.accessToken = accessToken;
        }

        @Override
        public void run() {
            Client client = ClientBuilder.newBuilder().register(SseFeature.class, JacksonContextResolver.class).build();
            WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/chat/join");
            webTarget.register(JacksonContextResolver.class);
            EventInput eventInput = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                    .header("Authorization", "Bearer " + accessToken.getValue())
                    .post(Entity.json(new ChatRequest(TEST_ROOM_ID)), EventInput.class);
            List<Message> expectedMessages = new ArrayList<>(Arrays.asList(new Message(adminUsername, "Hello World!"), new Message("testuser1", "Hello World!"), new Message(adminUsername, "EOM")));
            while (!eventInput.isClosed()) {
                final InboundEvent inboundEvent = eventInput.read();
                if (inboundEvent == null) {
                    break;
                }
                assertThat(expectedMessages.size(), greaterThan(0));
                Message expectedMessage = expectedMessages.remove(0);
                Message actualMessage = inboundEvent.readData(Message.class, MediaType.APPLICATION_JSON_TYPE);
                assertThat(actualMessage.getMessage(), is(expectedMessage.getMessage()));
                assertThat(actualMessage.getSender(), is(expectedMessage.getSender()));
            }
        }
    }

    private final class ChatMessageSender implements Runnable {

        private final OAuth2AccessToken accessToken;
        private final int sleepTime;

        public ChatMessageSender(OAuth2AccessToken accessToken, int sleepTime) {
            this.accessToken = accessToken;
            this.sleepTime = sleepTime;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
            WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/chat/send");
            Response post = webTarget.request()
                    .header("Authorization", "Bearer " + accessToken.getValue())
                    .post(Entity.json(new ChatRequest(TEST_ROOM_ID, "Hello World!")));
            assertThat(post.getStatus(), is(200));
        }
    }

    private final class ChatStreamCloser implements Runnable {

        private final OAuth2AccessToken accessToken;
        private final int sleepTime;

        public ChatStreamCloser(OAuth2AccessToken accessToken, int sleepTime) {
            this.accessToken = accessToken;
            this.sleepTime = sleepTime;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
            WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/chat/close");
            Response post = webTarget.request()
                    .header("Authorization", "Bearer " + accessToken.getValue())
                    .post(Entity.json(new ChatRequest(TEST_ROOM_ID)));
            assertThat(post.getStatus(), is(200));
        }
    }


}