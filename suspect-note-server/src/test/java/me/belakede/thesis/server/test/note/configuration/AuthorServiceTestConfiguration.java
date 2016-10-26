package me.belakede.thesis.server.test.note.configuration;

import me.belakede.thesis.server.note.repository.AuthorRepository;
import me.belakede.thesis.server.note.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthorServiceTestConfiguration {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuthorRepository repository;

    @Bean
    public AuthorService authorService() {
        return new AuthorService(repository);
    }

}
