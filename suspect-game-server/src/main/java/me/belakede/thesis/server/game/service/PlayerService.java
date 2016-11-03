package me.belakede.thesis.server.game.service;


import me.belakede.thesis.game.Game;
import me.belakede.thesis.server.game.converter.GameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // TODO rename GameService to GameStartService and PlayerService to GameService
public class PlayerService {

    private final GameConverter gameConverter;
    private Game gameLogic;
    private me.belakede.thesis.server.game.domain.Game gameEntity;

    @Autowired
    public PlayerService(GameConverter gameConverter) {
        this.gameConverter = gameConverter;
    }

    public void setGame(me.belakede.thesis.server.game.domain.Game gameEntity) {
        this.gameEntity = gameEntity;
        this.gameLogic = gameConverter.convert(gameEntity);
    }

}
