package me.belakede.thesis.server.chat.controller;

import me.belakede.thesis.server.chat.domain.Room;
import me.belakede.thesis.server.chat.exception.MissingRoomException;
import me.belakede.thesis.server.chat.service.RoomService;
import me.belakede.thesis.server.chat.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {

    private final RoomService roomService;
    private final SenderService senderService;

    @Autowired
    public RoomController(RoomService roomService, SenderService senderService) {
        this.roomService = roomService;
        this.senderService = senderService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Room> list() {
        return roomService.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Room create() {
        return roomService.create();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void join(Principal principal, @NotNull String name) throws MissingRoomException {
        Room room = roomService.findByName(name);
        if (room == null) {
            throw new MissingRoomException("The following room is not found: " + name);
        }
        senderService.create(principal.getName(), room);
    }

}
