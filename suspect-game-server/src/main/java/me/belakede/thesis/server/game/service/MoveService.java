package me.belakede.thesis.server.game.service;

import me.belakede.thesis.game.board.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MoveService {

    private final GameLogicService gameLogicService;
    private final PositionService positionService;

    @Autowired
    public MoveService(GameLogicService gameLogicService, PositionService positionService) {
        this.gameLogicService = gameLogicService;
        this.positionService = positionService;
    }

    public void move(int row, int column) {
        move(row, column);
        update();
    }

    private void moveCurrentPlayer(int row, int column) {
        gameLogicService.getGameLogic().move(getField(row, column));
    }

    private void update() {
        positionService.update();
    }

    private Field getField(int row, int column) {
        return gameLogicService.getGameLogic().getBoard().getField(row, column);
    }

}
