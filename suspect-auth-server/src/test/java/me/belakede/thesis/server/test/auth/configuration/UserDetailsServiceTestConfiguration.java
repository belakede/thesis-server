package me.belakede.thesis.server.test.auth.configuration;

import me.belakede.thesis.server.auth.repository.UserRepository;
import me.belakede.thesis.server.auth.service.DefaultUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class UserDetailsServiceTestConfiguration {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    @Bean
    public UserDetailsService userService() {
        return new DefaultUserDetailsService(repository);
    }

}
