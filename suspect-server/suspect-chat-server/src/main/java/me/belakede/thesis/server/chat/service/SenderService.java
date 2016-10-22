package me.belakede.thesis.server.chat.service;

import me.belakede.thesis.server.chat.domain.Sender;
import me.belakede.thesis.server.chat.exception.MissingSenderException;
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

    public Sender create(String name, String room) {
        return repository.save(new Sender(name, room));
    }

    public Sender findByNameAndRoom(String name, String room) throws MissingSenderException {
        Sender sender = repository.findByNameAndRoom(name, room);
        if (sender == null) {
            throw new MissingSenderException("The following sender is not found: " + name);
        }
        return sender;
    }

}
