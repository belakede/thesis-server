package me.belakede.thesis.server.note.controller;

import me.belakede.junit.OrderedSpringRunner;
import me.belakede.test.security.oauth2.OAuth2Helper;
import me.belakede.thesis.game.equipment.Marker;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.server.auth.domain.Role;
import me.belakede.thesis.server.note.request.NoteRequest;
import me.belakede.thesis.server.note.response.NoteResponse;
import me.belakede.thesis.server.note.response.NotesResponse;
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
import static org.hamcrest.Matchers.*;

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
    private OAuth2Helper authHelper;

    private OAuth2AccessToken accessToken;
    private OAuth2AccessToken testAccessToken;
    private NoteRequest request;

    @Before
    public void setUp() throws Exception {
        accessToken = authHelper.createOAuth2AccessToken(adminUsername, Role.ADMIN.getAuthority());
        testAccessToken = authHelper.createOAuth2AccessToken("testuser", Role.USER.getAuthority());
        request = new NoteRequest("test-room", Suspect.GREEN, "demo", Marker.YES);
    }

    @Test
    @Order(1)
    public void joinShouldCreateAnAuthorForAdmin() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/notes/join");
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .post(Entity.json(new NoteRequest("test-room")));
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

    @Test
    @Order(2)
    public void storeShouldCreateANoteAndSaveForLater() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/notes");
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .post(Entity.json(request));
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

    @Test
    @Order(3)
    public void findAllShouldThrowMissingAuthorException() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/notes").path("test-room");
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + testAccessToken.getValue())
                .get();
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    @Order(4)
    public void storeShouldThrowMissingAuthorException() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/notes");
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + testAccessToken.getValue())
                .post(Entity.json(request));
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    @Order(5)
    public void joinShouldCreateAnAuthorForTestUser() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/notes/join");
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + testAccessToken.getValue())
                .post(Entity.json(new NoteRequest("test-room")));
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

    @Test
    @Order(6)
    public void findAllShouldReturnAnEmptyListIfUserHasNotCreateAnyNotes() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/notes").path("test-room");
        NotesResponse response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + testAccessToken.getValue())
                .get(NotesResponse.class);
        assertThat(response, not(nullValue()));
        assertThat(response.getNotes().size(), is(0));
        assertThat(response.getNotes(), not(contains(new NoteResponse(request.getCard(), request.getOwner(), request.getMarker()))));
    }

    @Test
    @Order(7)
    public void findAllShouldReturnAListWithAllPreviouslyStoredNote() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/notes").path("test-room");
        NotesResponse response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .get(NotesResponse.class);
        assertThat(response, not(nullValue()));
        assertThat(response.getNotes().size(), greaterThan(0));
        assertThat(response.getNotes(), contains(new NoteResponse(request.getCard(), request.getOwner(), request.getMarker())));
    }


}