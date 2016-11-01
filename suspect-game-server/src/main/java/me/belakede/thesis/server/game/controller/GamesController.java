package me.belakede.thesis.server.game.controller;

import me.belakede.thesis.server.game.domain.Game;
import me.belakede.thesis.server.game.domain.Player;
import me.belakede.thesis.server.game.request.GamesRequest;
import me.belakede.thesis.server.game.response.GamesResponse;
import me.belakede.thesis.server.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/games")
public class GamesController {

    private final GameService gameService;

    @Autowired
    public GamesController(GameService gameService) {
        this.gameService = gameService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public GamesResponse create(@RequestBody GamesRequest gamesRequest) {
        Game game = gameService.create(gamesRequest.getBoardType(), gamesRequest.getUsers());
        Set<String> users = game.getPlayers().stream().map(p -> p.getUsername()).collect(Collectors.toSet());
        return new GamesResponse(game.getId(), game.getBoardType(), users);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        gameService.remove(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<GamesResponse> games() {
        return gameService.findAll().stream()
                .map(g -> new GamesResponse(g.getId(), g.getBoardType(), g.getPlayers().stream()
                        .map(Player::getUsername)
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

}
