package me.belakede.thesis.server.game.service;

import me.belakede.thesis.server.game.exception.GameIsAlreadyRunningException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
class JoinService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JoinService.class);

    private final GameService gameService;
    private final PlayerService playerService;

    @Autowired
    public JoinService(GameService gameService, PlayerService playerService) {
        this.gameService = gameService;
        this.playerService = playerService;
    }

    public SseEmitter join(String username) {
        LOGGER.info("{} tries to join to the game", username);
        if (!gameService.isRunning() && gameService.isAvailable()) {
            return playerService.join(username);
        }
        throw new GameIsAlreadyRunningException("Game is already running.");
    }

}
