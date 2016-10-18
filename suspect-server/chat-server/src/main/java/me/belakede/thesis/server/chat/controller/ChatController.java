package me.belakede.thesis.server.chat.controller;

import me.belakede.thesis.server.chat.domain.Message;
import me.belakede.thesis.server.chat.domain.Sender;
import me.belakede.thesis.server.chat.service.MessageService;
import me.belakede.thesis.server.chat.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/chat")
@EnableResourceServer
public class ChatController {

    private final MessageService messageService;
    private final SenderService senderService;

    @Autowired
    public ChatController(MessageService messageService, SenderService senderService) {
        this.messageService = messageService;
        this.senderService = senderService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void send(Principal principal, String message) {
        Sender sender = senderService.findByName(principal.getName());
        Message stored = messageService.create(sender, message);
    }

}
