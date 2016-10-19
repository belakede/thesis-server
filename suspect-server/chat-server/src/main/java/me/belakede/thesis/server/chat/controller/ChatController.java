package me.belakede.thesis.server.chat.controller;

import me.belakede.thesis.server.chat.domain.Sender;
import me.belakede.thesis.server.chat.exception.MissingRoomException;
import me.belakede.thesis.server.chat.exception.MissingSenderException;
import me.belakede.thesis.server.chat.service.EmitterService;
import me.belakede.thesis.server.chat.service.MessageService;
import me.belakede.thesis.server.chat.service.RoomService;
import me.belakede.thesis.server.chat.service.SenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.constraints.NotNull;
import java.security.Principal;

@RestController
@RequestMapping("/chat")
@EnableResourceServer
public class ChatController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

    private final MessageService messageService;
    private final SenderService senderService;
    private final RoomService roomService;
    private final EmitterService emitterService;

    @Autowired
    public ChatController(MessageService messageService, SenderService senderService, RoomService roomService, EmitterService emitterService) {
        this.messageService = messageService;
        this.senderService = senderService;
        this.roomService = roomService;
        this.emitterService = emitterService;
    }

    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public SseEmitter join(Principal principal, @NotNull String name) throws MissingRoomException {
        senderService.create(principal.getName(), roomService.findByName(name));
        return emitterService.createEmitter();
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public void send(Principal principal, @NotNull String message, @NotNull String room) throws MissingSenderException, MissingRoomException {
        LOGGER.info("{} would like to send the following message: {}", principal.getName(), message);
        Sender sender = senderService.findByNameAndRoom(principal.getName(), roomService.findByName(room));
        emitterService.broadcast(messageService.create(sender, message));
    }

}
