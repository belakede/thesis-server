package me.belakede.thesis.server.chat.service;

import me.belakede.thesis.server.chat.domain.Message;
import me.belakede.thesis.server.chat.domain.Sender;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    public Message create(Sender sender, String message) {
        return new Message(sender.getName(), message);
    }
}
