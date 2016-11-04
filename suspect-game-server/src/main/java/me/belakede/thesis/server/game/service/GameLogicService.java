package me.belakede.thesis.server.game.service;

import me.belakede.thesis.server.game.converter.GameConverter;
import me.belakede.thesis.server.game.domain.Player;
import me.belakede.thesis.server.game.repository.PlayerRepository;
import me.belakede.thesis.server.game.response.GameStatusNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameLogicService {

    private final GameConverter gameConverter;
    private final PlayerRepository playerRepository;
    private me.belakede.thesis.game.Game gameLogic;
    private me.belakede.thesis.server.game.domain.Game gameEntity;

    @Autowired
    public GameLogicService(GameConverter gameConverter, PlayerRepository playerRepository) {
        this.gameConverter = gameConverter;
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

    public void setGame(me.belakede.thesis.server.game.domain.Game game) {
        gameEntity = game;
        gameLogic = gameConverter.convert(game);
    }

    public void setGame(me.belakede.thesis.game.Game game) {
        gameLogic = game;
        gameEntity = gameConverter.convert(game);
    }

}
