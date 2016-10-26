package me.belakede.thesis.server.note.controller;

import me.belakede.junit.OrderedSpringRunner;
import me.belakede.test.security.oauth2.OAuth2Helper;
import me.belakede.thesis.game.equipment.Marker;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.server.auth.domain.Role;
import me.belakede.thesis.server.note.domain.Author;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.context.WebApplicationContext;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@Import(OAuth2Helper.class)
@RunWith(OrderedSpringRunner.class)
@ComponentScan("me.belakede.thesis.server.auth")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoteControllerIntegrationTest {

    @Value("${suspect.server.admin.username}")
    private String adminUsername;
    @LocalServerPort
    private int randomServerPort;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private OAuth2Helper authHelper;

    private OAuth2AccessToken accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = authHelper.createOAuth2AccessToken(adminUsername, Role.ADMIN.getAuthority());
    }

    @Test
    @Order(1)
    public void joinShouldCreateAnAuthor() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/notes/join").queryParam("room", "test-room");
        Author author = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .post(Entity.entity(null, "text/plain"), Author.class);
        assertThat(author.getRoom(), is("test-room"));
        assertThat(author.getName(), is(adminUsername));
        assertThat(author.getNotes(), nullValue());
        assertThat(author.getSuggestion(), nullValue());
    }

    @Test
    @Order(2)
    public void storeShouldCreateANoteAndSaveForLater() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/notes")
                .queryParam("room", "test-room")
                .queryParam("card", Suspect.GREEN)
                .queryParam("owner", "demo")
                .queryParam("marker", Marker.YES);
        webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .post(Entity.entity(null, "text/plain"));

//        assertThat(note.getCard(), is(Suspect.GREEN));
//        assertThat(note.getOwner(), is("demo"));
//        assertThat(note.getMarker(), is(Marker.YES));
    }


}