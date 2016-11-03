package me.belakede.thesis.server.game.service;

import me.belakede.thesis.server.game.domain.Game;
import me.belakede.thesis.server.game.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private Game game;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}
