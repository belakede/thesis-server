package me.belakede.thesis.server.note.controller;

import me.belakede.junit.OrderedSpringRunner;
import me.belakede.test.security.oauth2.OAuth2Helper;
import me.belakede.thesis.game.equipment.Room;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Suspicion;
import me.belakede.thesis.game.equipment.Weapon;
import me.belakede.thesis.internal.game.equipment.DefaultSuspicion;
import me.belakede.thesis.server.auth.domain.Role;
import me.belakede.thesis.server.note.domain.Suggestion;
import org.glassfish.jersey.logging.LoggingFeature;
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
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@Import(OAuth2Helper.class)
@RunWith(OrderedSpringRunner.class)
@ComponentScan("me.belakede.thesis.server.auth")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SuggestionControllerIntegrationTest {

    @Value("${suspect.server.admin.username}")
    private String adminUsername;
    @LocalServerPort
    private int randomServerPort;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private OAuth2Helper authHelper;

    private OAuth2AccessToken accessToken;
    private Suspicion suspicion;


    @Before
    public void setUp() throws Exception {
        accessToken = authHelper.createOAuth2AccessToken(adminUsername, Role.ADMIN.getAuthority());
        suspicion = new DefaultSuspicion(Suspect.SCARLET, Room.LIBRARY, Weapon.REVOLVER);
    }

    @Test
    @Order(1)
    public void readShouldThrowMissingAuthorExceptionWhenAuthorNotFoundInRoom() throws Exception {
        Logger logger = Logger.getLogger(getClass().getName());
        Feature feature = new LoggingFeature(logger, Level.INFO, null, null);
        Client client = ClientBuilder.newBuilder().register(feature).build();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/suggestions").queryParam("room", "test-room");
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .get();
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
        assertThat(response.readEntity(String.class), containsString("MissingAuthorException"));
    }

    @Test
    @Order(2)
    public void storeShouldThrowMissingAuthorExceptionWhenAuthorNotFoundInRoom() throws Exception {
        Logger logger = Logger.getLogger(getClass().getName());
        Feature feature = new LoggingFeature(logger, Level.INFO, null, null);
        Client client = ClientBuilder.newBuilder().register(feature).build();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/suggestions/test-room");
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .post(Entity.entity(suspicion, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
        assertThat(response.readEntity(String.class), containsString("MissingAuthorException"));
    }


    @Test
    @Order(3)
    public void readShouldThrowMissingSuggestionExceptionWhenAuthorDidNotSuggestAnything() throws Exception {
        Client client = ClientBuilder.newClient();
        client.target("http://localhost:" + randomServerPort + "/notes/join")
                .queryParam("room", "test-room")
                .request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .post(Entity.entity(null, "text/plain"));
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/suggestions").queryParam("room", "test-room");
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .get();
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
        assertThat(response.readEntity(String.class), containsString("MissingSuggestionException"));
    }

    @Test
    @Order(4)
    public void storeShouldReturnWithTheStoredSuggestion() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/suggestions").queryParam("room", "test-room");
        Suggestion suggestion = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .post(Entity.entity(suspicion, MediaType.APPLICATION_JSON_TYPE), Suggestion.class);

        assertThat(suggestion.getAuthor().getRoom(), is("test-room"));
        assertThat(suggestion.getRoom(), is(suspicion.getRoom()));
        assertThat(suggestion.getSuspect(), is(suspicion.getSuspect()));
        assertThat(suggestion.getWeapon(), is(suspicion.getWeapon()));
    }

    @Test
    @Order(5)
    public void readShouldReturnTheStoredSuggestion() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/suggestions").queryParam("room", "test-room");
        Suggestion suggestion = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .get(Suggestion.class);
        assertThat(suggestion.getAuthor().getRoom(), is("test-room"));
        assertThat(suggestion.getRoom(), is(suspicion.getRoom()));
        assertThat(suggestion.getSuspect(), is(suspicion.getSuspect()));
        assertThat(suggestion.getWeapon(), is(suspicion.getWeapon()));
    }


}