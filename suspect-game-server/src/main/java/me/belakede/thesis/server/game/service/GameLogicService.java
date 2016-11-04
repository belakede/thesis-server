package me.belakede.thesis.server.game.service;

import me.belakede.thesis.server.game.converter.GameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameLogicService {

    private final GameConverter gameConverter;
    private me.belakede.thesis.game.Game gameLogic;
    private me.belakede.thesis.server.game.domain.Game gameEntity;

    @Autowired
    public GameLogicService(GameConverter gameConverter) {
        this.gameConverter = gameConverter;
    }

    public me.belakede.thesis.game.Game getGameLogic() {
        return gameLogic;
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
