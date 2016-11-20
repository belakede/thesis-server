package me.belakede.thesis.server.game.service;

import org.springframework.stereotype.Service;

@Service
class NextService {

    private final GameService gameService;
    private final PlayerService playerService;

    public NextService(GameService gameService, PlayerService playerService) {
        this.gameService = gameService;
        this.playerService = playerService;
    }

    void next() {
        gameService.getGameLogic().next();
        playerService.next();
    }
}
