package me.belakede.thesis.server.game.service;

import me.belakede.thesis.game.field.Field;
import me.belakede.thesis.server.game.domain.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class MoveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoveService.class);

    private final GameService gameService;
    private final PositionService positionService;

    @Autowired
    public MoveService(GameService gameService, PositionService positionService) {
        this.gameService = gameService;
        this.positionService = positionService;
    }

    void move(int row, int column) {
        if (!gameService.getLastAction().equals(Action.MOVE)) {
            moveCurrentPlayer(row, column);
            updatePosition();
            gameService.changeLastAction(Action.MOVE);
        }
    }

    private void moveCurrentPlayer(int row, int column) {
        Field field = getField(row, column);
        LOGGER.debug("Move current player to {}", field);
        gameService.getGameLogic().move(field);
    }

    private void updatePosition() {
        positionService.update();
    }

    private Field getField(int row, int column) {
        return gameService.getGameLogic().getBoard().getField(row, column);
    }

}
