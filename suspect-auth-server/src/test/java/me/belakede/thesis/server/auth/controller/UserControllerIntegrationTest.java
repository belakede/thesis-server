package me.belakede.thesis.server.auth.controller;

import me.belakede.junit.OrderedSpringRunner;
import me.belakede.test.security.oauth2.OAuth2Helper;
import me.belakede.thesis.server.auth.domain.Role;
import me.belakede.thesis.server.auth.request.UserRequest;
import me.belakede.thesis.server.auth.response.UserResponse;
import me.belakede.thesis.server.auth.response.UsersResponse;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Import(OAuth2Helper.class)
@RunWith(OrderedSpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {

    private static final String TEST_USER = "testuser1";

    @Value("${suspect.server.admin.username}")
    private String adminUsername;
    @LocalServerPort
    private int randomServerPort;
    @Autowired
    private OAuth2Helper authHelper;

    private OAuth2AccessToken accessToken;

    @Before
    public void setup() {
        accessToken = authHelper.createOAuth2AccessToken(adminUsername, Role.ADMIN.getAuthority());
    }

    @Test
    @Order(1)
    public void getUsersShouldReturnAnErrorMessageWhenUserIsNotAuthenticated() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/users");
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE).get();
        assertThat(response.getStatus(), is(Response.Status.UNAUTHORIZED.getStatusCode()));
    }

    @Test
    @Order(2)
    public void createUserShouldReturnWithTheCreatedUser() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/users");
        UserResponse response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .post(Entity.json(new UserRequest(TEST_USER, "password")), UserResponse.class);

        assertThat(response.getUsername(), is(TEST_USER));
    }

    @Test
    @Order(3)
    public void getUsersShouldReturnAListWithAllRegisteredUsersWithRoleUser() throws Exception {
        Logger logger = Logger.getLogger(getClass().getName());
        Feature feature = new LoggingFeature(logger, Level.INFO, null, null);
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).register(feature).build();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/users");
        UsersResponse response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .get(UsersResponse.class);
        assertThat(response.getUsers().size(), greaterThan(0));
        assertThat(response.getUsers(), contains(new UserResponse(TEST_USER)));
        assertThat(response.getUsers(), not(contains(new UserResponse(adminUsername))));
    }

    @Test
    @Order(4)
    public void removeUserShouldReturnWithTheRemovedUser() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/users").path(TEST_USER);
        UserResponse response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .delete(UserResponse.class);

        assertThat(response.getUsername(), is(TEST_USER));
    }

    @Test
    @Order(5)
    public void removeUserShouldThrowUserNotFoundException() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:" + randomServerPort + "/users").path(TEST_USER);
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken.getValue())
                .delete();

        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
        assertThat(response.readEntity(String.class), containsString("MissingUserException"));
    }

}