package me.belakede.thesis.server.game.controller;

import me.belakede.thesis.server.game.service.GameService;
import me.belakede.thesis.server.game.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.security.Principal;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final NotificationService notificationService;

    @Autowired
    public GameController(GameService gameService, NotificationService notificationService) {
        this.gameService = gameService;
        this.notificationService = notificationService;
    }

    @RequestMapping(value = "/join", method = RequestMethod.GET)
    public SseEmitter join(Principal principal) {
        return gameService.createEmitter(principal.getName());
    }


}
