package me.belakede.thesis.server.game.service;

import me.belakede.thesis.game.equipment.BoardType;
import me.belakede.thesis.internal.game.util.GameBuilder;
import me.belakede.thesis.server.auth.domain.User;
import me.belakede.thesis.server.auth.exception.MissingUserException;
import me.belakede.thesis.server.auth.service.UserService;
import me.belakede.thesis.server.game.converter.GameConverter;
import me.belakede.thesis.server.game.domain.Game;
import me.belakede.thesis.server.game.exception.MissingBoardException;
import me.belakede.thesis.server.game.exception.MissingGameException;
import me.belakede.thesis.server.game.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LobbyService {

    private final GameRepository gameRepository;
    private final GameConverter gameConverter;
    private final UserService userService;

    @Autowired
    public LobbyService(GameRepository gameRepository, GameConverter gameConverter, UserService userService) {
        this.gameRepository = gameRepository;
        this.gameConverter = gameConverter;
        this.userService = userService;
    }

    /**
     * @param boardType
     * @param usernames
     * @return
     * @throws MissingUserException
     * @throws MissingBoardException
     */
    public Game create(BoardType boardType, Collection<String> usernames) {
        try {
            List<User> users = usernames.stream().map(u -> userService.findByUsername(u)).collect(Collectors.toList());
            me.belakede.thesis.game.Game gameLogic = GameBuilder.create().boardType(boardType).mystery().players(users.size()).positions().build();
            Game game = gameConverter.convert(gameLogic);
            for (int i = 0; i < game.getPlayers().size(); i++) {
                game.getPlayers().get(i).setUsername(users.get(i).getUsername());
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
