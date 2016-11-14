package me.belakede.thesis.server.game.service;

import me.belakede.thesis.game.equipment.BoardType;
import me.belakede.thesis.internal.game.util.GameBuilder;
import me.belakede.thesis.server.game.converter.GameConverter;
import me.belakede.thesis.server.game.domain.Game;
import me.belakede.thesis.server.game.exception.MissingBoardException;
import me.belakede.thesis.server.game.exception.MissingGameException;
import me.belakede.thesis.server.game.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class LobbyService {

    private final GameRepository gameRepository;
    private final GameConverter gameConverter;

    @Autowired
    public LobbyService(GameRepository gameRepository, GameConverter gameConverter) {
        this.gameRepository = gameRepository;
        this.gameConverter = gameConverter;
    }

    /**
     * @param boardType
     * @param users
     * @return
     * @throws MissingBoardException
     */
    public Game create(BoardType boardType, List<String> users) {
        try {
            me.belakede.thesis.game.Game gameLogic = GameBuilder.create().boardType(boardType).mystery().players(users.size()).positions().build();
            Game game = gameConverter.convert(gameLogic);
            for (int i = 0; i < game.getPlayers().size(); i++) {
                game.getPlayers().get(i).setUsername(users.get(i));
            }
            return gameRepository.saveAndFlush(game);
        } catch (IOException e) {
            throw new MissingBoardException("Board not found: " + boardType);
        }
    }

    public void remove(Long id) {
        gameRepository.delete(id);
    }

    public List<Game> findAll() {
        return gameRepository.findAll();
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
