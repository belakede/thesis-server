package me.belakede.thesis.server.chat.service;

import me.belakede.thesis.server.chat.domain.Room;
import me.belakede.thesis.server.chat.domain.Sender;
import me.belakede.thesis.server.chat.repository.SenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SenderService {

    private final SenderRepository repository;

    @Autowired
    public SenderService(SenderRepository repository) {
        this.repository = repository;
    }

    public Sender create(String name, Room room) {
        return repository.save(new Sender(name, room));
    }

}
