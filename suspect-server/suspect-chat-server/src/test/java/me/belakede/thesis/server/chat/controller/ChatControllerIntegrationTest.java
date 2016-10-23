package me.belakede.thesis.server.chat.controller;

import me.belakede.junit.OrderedSpringRunner;
import me.belakede.test.security.oauth2.OAuth2Helper;
import me.belakede.thesis.server.auth.domain.Role;
import me.belakede.thesis.server.auth.domain.User;
import me.belakede.thesis.server.auth.repository.UserRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Import(OAuth2Helper.class)
@RunWith(OrderedSpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatControllerIntegrationTest {

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
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        mockMvc.perform(post("/users").with(createOAuthProcessor(adminUsername, Role.ADMIN.getAuthority())).param("username", TEST_USER).param("password", "password")).andDo(print());
    }

    private RequestPostProcessor createOAuthProcessor(String username, String... authorities) {
        return authHelper.addBearerToken(username, authorities);
    }

}