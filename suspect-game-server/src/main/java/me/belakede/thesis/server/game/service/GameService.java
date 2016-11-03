package me.belakede.thesis.server.game.service;

import me.belakede.thesis.server.game.domain.Game;
import me.belakede.thesis.server.game.exception.MissingGameException;
import me.belakede.thesis.server.game.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /**
     * @param id
     * @return
     * @throws MissingGameException
     */
    public Game findById(Long id) {
        Game game = gameRepository.findOne(id);
        if (game == null) {
            throw new MissingGameException("Game not found with id: " + id);
        }
        return game;
    }
}
