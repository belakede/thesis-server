package me.belakede.thesis.server.auth.controller;

import me.belakede.junit.OrderedSpringRunner;
import me.belakede.test.security.oauth2.OAuth2Helper;
import me.belakede.thesis.server.auth.domain.User;
import me.belakede.thesis.server.auth.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(OAuth2Helper.class)
@RunWith(OrderedSpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {

    private static final String TEST_USER = "testuser1";

    @Autowired
    private UserRepository repository;
    @Value("${suspect.server.admin.username}")
    private String adminUsername;

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private OAuth2Helper authHelper;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @Order(1)
    public void getUsersShouldReturnAnErrorMessageWhenUserIsNotAuthenticated() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/users")).andDo(print());
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @Order(2)
    public void createUserShouldReturnWithTheCreatedUser() throws Exception {
        User user = repository.findByUsername(adminUsername);
        RequestPostProcessor bearerToken = authHelper.addBearerToken(user.getUsername(), user.getRoles().stream().map(Enum::name).toArray(String[]::new));
        ResultActions resultActions = mockMvc.perform(post("/users").with(bearerToken).param("username", TEST_USER).param("password", "password")).andDo(print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"username\":\"" + TEST_USER + "\"")))
                .andExpect(content().string(containsString("\"roles\":[\"USER\"]")))
                .andExpect(content().string(containsString("\"enabled\":true")));
    }

    @Test
    @Order(3)
    public void getUsersShouldReturnAListWithAllRegisteredUsersWithRoleUser() throws Exception {
        User user = repository.findByUsername(adminUsername);
        RequestPostProcessor bearerToken = authHelper.addBearerToken(user.getUsername(), user.getRoles().stream().map(Enum::name).toArray(String[]::new));
        ResultActions resultActions = mockMvc.perform(get("/users").with(bearerToken)).andDo(print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("\"username\":\"admin\""))))
                .andExpect(content().string(containsString("\"username\":\"" + TEST_USER + "\"")));
    }

    @Test
    @Order(4)
    public void removeUserShouldReturnWithTheRemovedUser() throws Exception {
        ResultActions resultActions;
        User user = repository.findByUsername(adminUsername);
        RequestPostProcessor bearerToken = authHelper.addBearerToken(user.getUsername(), user.getRoles().stream().map(Enum::name).toArray(String[]::new));
        resultActions = mockMvc.perform(delete("/users").with(bearerToken).param("username", TEST_USER)).andDo(print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"username\":\"" + TEST_USER + "\"")))
                .andExpect(content().string(containsString("\"roles\":[\"USER\"]")))
                .andExpect(content().string(containsString("\"enabled\":true")));
        resultActions = mockMvc.perform(get("/users").with(bearerToken)).andDo(print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("\"username\":\"" + TEST_USER + "\""))));
    }

}