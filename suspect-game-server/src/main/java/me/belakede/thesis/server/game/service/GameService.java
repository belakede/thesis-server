package me.belakede.thesis.server.game.service;

import me.belakede.thesis.server.game.domain.Game;
import me.belakede.thesis.server.game.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final NotificationService notificationService;
    private Game game;

    @Autowired
    public GameService(GameRepository gameRepository, NotificationService notificationService) {
        this.gameRepository = gameRepository;
        this.notificationService = notificationService;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean gameInProgress() {
        return game != null;
    }

    public SseEmitter createEmitter(String username) {
        return notificationService.createEmitter(username);
    }


}
