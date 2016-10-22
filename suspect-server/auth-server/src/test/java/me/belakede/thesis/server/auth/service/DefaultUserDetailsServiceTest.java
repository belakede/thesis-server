package me.belakede.thesis.server.auth.service;

import me.belakede.thesis.server.auth.domain.Role;
import me.belakede.thesis.server.auth.domain.User;
import me.belakede.thesis.server.test.auth.configuration.UserDetailsServiceTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashSet;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
@Import(UserDetailsServiceTestConfiguration.class)
public class DefaultUserDetailsServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameShouldThrowExceptionWhenUsernameNotFound() {
        userDetailsService.loadUserByUsername("testuser1");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameShouldThrowExceptionWhenUsernameIsNull() {
        userDetailsService.loadUserByUsername(null);
    }

    @Test
    public void loadUserByUsernameShouldReturnADefaultUserDetailsContainsTheSpecifiedUser() {
        User expectedUser = new User("user", "password", true, new HashSet<>(Collections.singletonList(Role.USER)));
        User persistedUser = entityManager.persist(expectedUser);
        UserDetails user = userDetailsService.loadUserByUsername("user");
        assertThat(user.getUsername(), is(persistedUser.getUsername()));
        assertThat(user.getPassword(), is(persistedUser.getPassword()));
        assertThat(user.getAuthorities().size(), is(1));
        assertThat(user.getAuthorities(), contains(Role.USER));
        assertThat(user.isEnabled(), is(true));
        assertThat(user.isAccountNonExpired(), is(true));
        assertThat(user.isAccountNonLocked(), is(true));
        assertThat(user.isCredentialsNonExpired(), is(true));
    }

}