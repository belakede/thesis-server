package me.belakede.thesis.server.game.controller;

import me.belakede.thesis.internal.game.equipment.DefaultSuspicion;
import me.belakede.thesis.internal.game.util.Cards;
import me.belakede.thesis.server.game.request.MoveRequest;
import me.belakede.thesis.server.game.request.ShowRequest;
import me.belakede.thesis.server.game.request.SuspectRequest;
import me.belakede.thesis.server.game.response.Coordinate;
import me.belakede.thesis.server.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.security.Principal;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @RequestMapping(value = "/join", method = RequestMethod.GET)
    public SseEmitter join(Principal principal) {
        return gameService.join(principal);
    }

    @RequestMapping(value = "/leave", method = RequestMethod.POST)
    public void leave(Principal principal) {
        gameService.exit(principal);
    }

    @RequestMapping(value = "/roll", method = RequestMethod.POST)
    public void roll(Principal principal) {
        gameService.roll(principal);
    }

    @RequestMapping(value = "/move", method = RequestMethod.POST)
    public void move(Principal principal, @RequestBody MoveRequest moveRequest) {
        gameService.move(principal, new Coordinate(moveRequest.getRow(), moveRequest.getColumn()));
    }

    @RequestMapping(value = "/next", method = RequestMethod.POST)
    public void next(Principal principal) {
        gameService.next(principal);
    }

    @RequestMapping(value = "/suspect", method = RequestMethod.POST)
    public void suspect(Principal principal, @RequestBody SuspectRequest suspectRequest) {
        gameService.suspect(principal, new DefaultSuspicion(suspectRequest.getSuspect(), suspectRequest.getRoom(), suspectRequest.getWeapon()));
    }

    @RequestMapping(value = "/show", method = RequestMethod.POST)
    public void show(Principal principal, @RequestBody ShowRequest showRequest) {
        gameService.show(principal, Cards.valueOf(showRequest.getCard()).get());
    }

    @RequestMapping(value = "/accuse", method = RequestMethod.POST)
    public void accuse(Principal principal, @RequestBody SuspectRequest suspectRequest) {
        gameService.accuse(principal, new DefaultSuspicion(suspectRequest.getSuspect(), suspectRequest.getRoom(), suspectRequest.getWeapon()));
    }
}
