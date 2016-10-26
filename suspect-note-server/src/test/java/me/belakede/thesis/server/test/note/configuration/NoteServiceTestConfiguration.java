package me.belakede.thesis.server.test.note.configuration;

import me.belakede.thesis.server.note.repository.NoteRepository;
import me.belakede.thesis.server.note.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NoteServiceTestConfiguration {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NoteRepository repository;

    @Bean
    public NoteService noteService() {
        return new NoteService(repository);
    }

}
