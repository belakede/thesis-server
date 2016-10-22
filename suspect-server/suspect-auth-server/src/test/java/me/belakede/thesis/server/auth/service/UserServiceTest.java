package me.belakede.thesis.server.auth.service;

import me.belakede.thesis.server.auth.domain.Role;
import me.belakede.thesis.server.auth.domain.User;
import me.belakede.thesis.server.auth.exception.MissingUserException;
import me.belakede.thesis.server.auth.repository.UserRepository;
import me.belakede.thesis.server.test.auth.configuration.UserServiceTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
@Import(UserServiceTestConfiguration.class)
public class UserServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Test
    public void findByRoleShouldReturnAnEmptyListWhenRoleIsUser() {
        List<User> users = userService.findByRole(Role.USER);
        assertThat(users, empty());
    }

    @Test
    public void findByRoleShouldReturnListContainsAdminUserWhenRoleIsAdmin() {
        User user = new User("admin", passwordEncoder.encode("password"), true, new HashSet<>(Collections.singletonList(Role.ADMIN)));
        entityManager.persist(user);
        List<User> users = userService.findByRole(Role.ADMIN);
        assertThat(users.size(), is(1));
        assertThat(users, contains(user));
    }

    @Test
    public void createShouldReturnWithCreatedUser() {
        User expectedUser = new User("user", passwordEncoder.encode("password"), true, new HashSet<>(Collections.singletonList(Role.USER)));
        User user = userService.create("user", "password");
        assertThat(user, is(expectedUser));
        assertThat(passwordEncoder.matches("password", user.getPassword()), is(true));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void createShouldThrowDataIntegrityViolationExceptionWhenUsernameIsNull() {
        userService.create(null, "password");
    }

    @Test(expected = NullPointerException.class)
    public void createShouldThrowNullPointerExceptionWhenPasswordIsNull() {
        userService.create("user", null);
    }

    @Test
    public void removeShouldRemoveTheSpecifiedUserFromTheDatasource() throws Exception {
        User expectedUser = new User("user", passwordEncoder.encode("password"), true, new HashSet<>(Collections.singletonList(Role.USER)));
        entityManager.persist(expectedUser);
        User removedUser = userService.remove("user");
        User user = entityManager.find(User.class, removedUser.getId());
        assertThat(removedUser, is(expectedUser));
        assertThat(user, nullValue());
    }

    @Test(expected = MissingUserException.class)
    public void removeShouldThrowExceptionWhenUserNotExists() throws Exception {
        userService.remove("user");
    }


}