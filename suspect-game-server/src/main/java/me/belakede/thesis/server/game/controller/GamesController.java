package me.belakede.thesis.server.game.controller;

import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.server.game.domain.Game;
import me.belakede.thesis.server.game.domain.Player;
import me.belakede.thesis.server.game.request.GamesRequest;
import me.belakede.thesis.server.game.response.GamesResponse;
import me.belakede.thesis.server.game.service.GameLogicService;
import me.belakede.thesis.server.game.service.LobbyService;
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

    private final LobbyService lobbyService;
    private final GameLogicService gameLogicService;

    @Autowired
    public GamesController(LobbyService lobbyService, GameLogicService gameLogicService) {
        this.lobbyService = lobbyService;
        this.gameLogicService = gameLogicService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public GamesResponse create(@RequestBody GamesRequest gamesRequest) {
        Game game = lobbyService.create(gamesRequest.getBoardType(), new ArrayList<>(gamesRequest.getUsers()));
        return new GamesResponse(game.getId(), game.getBoardType(), createUsers(game.getPlayers()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        lobbyService.remove(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<GamesResponse> games() {
        return lobbyService.findAll().stream()
                .map(g -> new GamesResponse(g.getId(), g.getBoardType(), createUsers(g.getPlayers())))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/start/{id}", method = RequestMethod.POST)
    public void start(@PathVariable("id") Long id) {
        Game game = lobbyService.findById(id);
        gameLogicService.setGame(game);
    }

    private Map<Suspect, String> createUsers(List<Player> players) {
        Map<Suspect, String> users = new HashMap<>();
        players.forEach(player -> users.put(player.getFigurine(), player.getUsername()));
        return users;
    }

}
