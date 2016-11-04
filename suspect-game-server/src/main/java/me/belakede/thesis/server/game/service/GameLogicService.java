package me.belakede.thesis.server.game.service;

import me.belakede.thesis.game.Game;
import org.springframework.stereotype.Component;

@Component
public class GameLogicService {
    private Game game;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
