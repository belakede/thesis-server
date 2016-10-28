package me.belakede.thesis.server.note.controller;

import me.belakede.junit.OrderedSpringRunner;
import me.belakede.test.security.oauth2.OAuth2Helper;
import me.belakede.thesis.game.equipment.Room;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;
import me.belakede.thesis.server.auth.domain.Role;
import me.belakede.thesis.server.note.request.NoteRequest;
import me.belakede.thesis.server.note.request.SuggestionRequest;
import me.belakede.thesis.server.note.response.SuggestionResponse;
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

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

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
    private OAuth2Helper authHelper;

    private OAuth2AccessToken accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = authHelper.createOAuth2AccessToken(adminUsername, Role.ADMIN.getAuthority());
    }

    @Test
    @Order(1)
    public void readShouldThrowMissingAuthorExceptionWhenAuthorNotFound() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/suggestions").path("test-room");
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .get();
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
        assertThat(response.readEntity(String.class), containsString("MissingAuthorException"));
    }

    @Test
    @Order(2)
    public void storeShouldThrowMissingAuthorExceptionWhenAuthorNotFound() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/suggestions").path("test-room");
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .post(Entity.json(new SuggestionRequest(Suspect.MUSTARD, Room.WORKROOM, Weapon.LEAD_PIPE)));
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
        assertThat(response.readEntity(String.class), containsString("MissingAuthorException"));
    }

    @Test
    @Order(3)
    public void readShouldThrowMissingSuggestionExceptionWhenAuthorDidNotSendAny() {
        joinToRoom();
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/suggestions").path("test-room");
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .get();
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
        assertThat(response.readEntity(String.class), containsString("MissingSuggestionException"));
    }

    @Test
    @Order(4)
    public void storeShouldSaveTheSentSuggestionStoreItAndReturnWithStatusOk() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/suggestions").path("test-room");
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .post(Entity.json(new SuggestionRequest(Suspect.MUSTARD, Room.WORKROOM, Weapon.LEAD_PIPE)));
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

    @Test
    @Order(5)
    public void readShouldReturnThePreviouslySentSuggestion() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/suggestions").path("test-room");
        SuggestionResponse response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .get(SuggestionResponse.class);
        assertThat(response.getSuspect(), is(Suspect.MUSTARD));
        assertThat(response.getRoom(), is(Room.WORKROOM));
        assertThat(response.getWeapon(), is(Weapon.LEAD_PIPE));
    }

    @Test
    @Order(6)
    public void storeShouldUpdateThePreviouslySavedSuggestionAndReturnWithStatusOk() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/suggestions").path("test-room");
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .post(Entity.json(new SuggestionRequest(Suspect.PEACOCK, Room.BILLIARD_ROOM, Weapon.ROPE)));
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

    @Test
    @Order(7)
    public void readShouldReturnWithTheNewSuggestion() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/suggestions").path("test-room");
        SuggestionResponse response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .get(SuggestionResponse.class);
        assertThat(response.getSuspect(), is(Suspect.PEACOCK));
        assertThat(response.getRoom(), is(Room.BILLIARD_ROOM));
        assertThat(response.getWeapon(), is(Weapon.ROPE));
    }

    private void joinToRoom() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/notes/join");
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .post(Entity.json(new NoteRequest("test-room")));
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

}