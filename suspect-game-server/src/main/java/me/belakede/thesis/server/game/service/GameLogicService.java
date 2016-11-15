package me.belakede.thesis.server.game.service;

import me.belakede.thesis.server.game.converter.GameConverter;
import me.belakede.thesis.server.game.domain.Game;
import me.belakede.thesis.server.game.domain.Player;
import me.belakede.thesis.server.game.repository.GameRepository;
import me.belakede.thesis.server.game.repository.PlayerRepository;
import me.belakede.thesis.server.game.response.GameStatusNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameLogicService {

    private final GameConverter gameConverter;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private me.belakede.thesis.game.Game gameLogic;
    private me.belakede.thesis.server.game.domain.Game gameEntity;

    @Autowired
    public GameLogicService(GameConverter gameConverter, GameRepository gameRepository, PlayerRepository playerRepository) {
        this.gameConverter = gameConverter;
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    public me.belakede.thesis.game.Game getGameLogic() {
        return gameLogic;
    }

    public me.belakede.thesis.server.game.domain.Game getGameEntity() {
        return gameEntity;
    }

    public GameStatusNotification getGameStatusNotification() {
        List<Player> players = playerRepository.findByGameOrderByOrdinalNumberAsc(gameEntity);
        return gameConverter.convert(gameEntity, players);
    }

    public boolean gameInProgress() {
        return gameLogic != null;
    }

    public void pauseTheGame() {
        setStatus(Game.Status.PAUSED);
        saveAndFlush();
        clearGames();
    }

    public void finishTheGame() {
        setStatus(Game.Status.FINISHED);
        saveAndFlush();
        clearGames();
    }

    public void setGame(me.belakede.thesis.server.game.domain.Game game) {
        gameEntity = game;
        gameLogic = gameConverter.convert(game);
        gameEntity.setStatus(Game.Status.IN_PROGRESS);
        gameRepository.save(gameEntity);
    }

    public void setGame(me.belakede.thesis.game.Game game) {
        gameLogic = game;
        gameEntity = gameConverter.convert(game);
        gameEntity.setStatus(Game.Status.IN_PROGRESS);
        gameRepository.save(gameEntity);
    }

    private void setStatus(Game.Status status) {
        gameEntity.setStatus(status);
    }

    private void saveAndFlush() {
        gameRepository.saveAndFlush(gameEntity);
    }

    private void clearGames() {
        gameLogic = null;
        gameEntity = null;
    }

}
