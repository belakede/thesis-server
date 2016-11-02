package me.belakede.thesis.server.game.service;

import me.belakede.thesis.game.equipment.BoardType;
import me.belakede.thesis.server.game.domain.Game;
import me.belakede.thesis.server.game.repository.GameRepository;
import me.belakede.thesis.server.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class LobbyService {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public LobbyService(GameRepository gameRepository, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    public Game create(BoardType boardType, Collection<String> users) {
        new Game(boardType, Game.Status.CREATED);
        // TODO add option to get users by name from the auth server
        return null;
    }

    public void remove(Long id) {
        gameRepository.delete(id);
    }

    public List<Game> findAll() {
        return gameRepository.findAll();
    }

}
