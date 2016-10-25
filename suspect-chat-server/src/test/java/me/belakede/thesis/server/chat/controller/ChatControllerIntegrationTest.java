package me.belakede.thesis.server.chat.controller;

import me.belakede.junit.OrderedSpringRunner;
import me.belakede.test.security.oauth2.OAuth2Helper;
import me.belakede.thesis.server.auth.domain.Role;
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
import org.springframework.web.context.WebApplicationContext;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
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
    private WebApplicationContext context;
    @Autowired
    private OAuth2Helper authHelper;


    @Test
    public void test() throws InterruptedException {
        OAuth2AccessToken accessToken = authHelper.createOAuth2AccessToken("admin", Role.ADMIN.getAuthority());
        Thread firstThread = new Thread(new FirstTest(accessToken));
        Thread secondThread = new Thread(new SecondTest(accessToken));
        Thread thirdThread = new Thread(new ThirdTest(accessToken));
        firstThread.start();
        secondThread.start();
        thirdThread.start();

        firstThread.join();
        secondThread.join();
        thirdThread.join();
    }

    private final class FirstTest implements Runnable {

        private final OAuth2AccessToken accessToken;

        public FirstTest(OAuth2AccessToken accessToken) {
            this.accessToken = accessToken;
        }

        @Override
        public void run() {
            Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
            WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/chat/join").queryParam("room", TEST_ROOM_ID);
            EventInput eventInput = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                    .header("Authorization", "Bearer " + accessToken.getValue())
                    .post(Entity.entity(null, "text/plain"), EventInput.class);
            while (!eventInput.isClosed()) {
                final InboundEvent inboundEvent = eventInput.read();
                if (inboundEvent == null) {
                    System.err.println("Connection has been closed!");
                    break;
                }
                System.out.println(inboundEvent.getName() + "; " + inboundEvent.readData(String.class));
            }
        }
    }

    private final class SecondTest implements Runnable {

        private final OAuth2AccessToken accessToken;

        public SecondTest(OAuth2AccessToken accessToken) {
            this.accessToken = accessToken;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
            WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/chat/send").queryParam("room", TEST_ROOM_ID).queryParam("message", "Hello World!");
            Response post = webTarget.request().header("Authorization", "Bearer " + accessToken.getValue()).post(Entity.entity(null, "text/plain"));
            assertThat(post.getStatus(), is(200));
        }
    }


    private final class ThirdTest implements Runnable {

        private final OAuth2AccessToken accessToken;

        public ThirdTest(OAuth2AccessToken accessToken) {
            this.accessToken = accessToken;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
            WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/chat/close").queryParam("room", TEST_ROOM_ID);
            Response post = webTarget.request().header("Authorization", "Bearer " + accessToken.getValue()).post(Entity.entity(null, "text/plain"));
            assertThat(post.getStatus(), is(200));
        }
    }



}