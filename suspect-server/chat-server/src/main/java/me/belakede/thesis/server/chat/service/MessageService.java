package me.belakede.thesis.server.chat.service;

import me.belakede.thesis.server.chat.domain.Message;
import me.belakede.thesis.server.chat.domain.Sender;
import me.belakede.thesis.server.chat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageRepository repository;

    @Autowired
    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    public Message create(Sender sender, String message) {
        return repository.save(new Message(sender, message));
    }
}
