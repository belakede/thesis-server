package me.belakede.thesis.server.chat.service;

import me.belakede.thesis.server.chat.domain.Message;
import me.belakede.thesis.server.chat.domain.Sender;
import me.belakede.thesis.server.chat.exception.MissingSenderException;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    public Message create(Sender sender, String message) throws MissingSenderException {
        if (sender == null) {
            throw new MissingSenderException("The specified sender is not valid.");
        }
        return new Message(sender.getName(), message);
    }
}
