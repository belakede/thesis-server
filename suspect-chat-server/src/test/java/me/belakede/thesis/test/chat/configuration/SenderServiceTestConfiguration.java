package me.belakede.thesis.test.chat.configuration;

import me.belakede.thesis.server.chat.repository.SenderRepository;
import me.belakede.thesis.server.chat.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SenderServiceTestConfiguration {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SenderRepository repository;

    @Bean
    public SenderService senderService() {
        return new SenderService(repository);
    }

}
