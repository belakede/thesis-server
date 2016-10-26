package me.belakede.thesis.server.test.note.configuration;

import me.belakede.thesis.server.note.repository.SuggestionRepository;
import me.belakede.thesis.server.note.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SuggestionServiceTestConfiguration {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SuggestionRepository repository;

    @Bean
    public SuggestionService noteService() {
        return new SuggestionService(repository);
    }

}
