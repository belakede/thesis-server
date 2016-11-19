package me.belakede.thesis.server.game.controller;

import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.server.game.domain.Game;
import me.belakede.thesis.server.game.domain.Player;
import me.belakede.thesis.server.game.domain.Status;
import me.belakede.thesis.server.game.exception.GameIsAlreadyRunningException;
import me.belakede.thesis.server.game.request.GamesRequest;
import me.belakede.thesis.server.game.response.GamesResponse;
import me.belakede.thesis.server.game.service.GameService;
import me.belakede.thesis.server.game.service.GamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/games")
public class GamesController {

    private final GamesService gamesService;
    private final GameService gameService;

    @Autowired
    public GamesController(GamesService gamesService, GameService gameService) {
        this.gamesService = gamesService;
        this.gameService = gameService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public GamesResponse create(@RequestBody GamesRequest gamesRequest) {
        Game game = gamesService.create(gamesRequest.getBoardType(), new ArrayList<>(gamesRequest.getUsers()));
        return new GamesResponse(game.getId(), game.getRoom(), game.getBoardType(), Status.valueOf(game.getStatus().name()), game.getCreated(), createUsers(game.getPlayers()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        if (gameService.isRunning()) {
            throw new GameIsAlreadyRunningException("A game is running. Please try later.");
        }
        gamesService.remove(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<GamesResponse> games() {
        return gamesService.findAll().stream()
                .map(g -> new GamesResponse(g.getId(), g.getRoom(), g.getBoardType(), Status.valueOf(g.getStatus().name()), g.getCreated(), createUsers(g.getPlayers())))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/start/{id}", method = RequestMethod.POST)
    public void start(@PathVariable("id") Long id) {
        if (gameService.isRunning()) {
            throw new GameIsAlreadyRunningException("A game is running. Please try later.");
        }
        gameService.setGameEntity(gamesService.findById(id));
    }

    private Map<Suspect, String> createUsers(List<Player> players) {
        Map<Suspect, String> users = new HashMap<>();
        players.forEach(player -> users.put(player.getFigurine(), player.getUsername()));
        return users;
    }

}
