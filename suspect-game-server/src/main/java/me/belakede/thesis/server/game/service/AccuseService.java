package me.belakede.thesis.server.game.service;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import me.belakede.thesis.game.equipment.Suspicion;
import me.belakede.thesis.server.game.converter.CardConverter;
import me.belakede.thesis.server.game.domain.Player;
import me.belakede.thesis.server.game.response.AccusationNotification;
import me.belakede.thesis.server.game.response.GameEndedNotification;
import me.belakede.thesis.server.game.response.Notification;
import me.belakede.thesis.server.game.response.PlayerOutNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccuseService {

    private final PlayerService playerService;
    private final CardConverter cardConverter;
    private final GameLogicService gameLogicService;
    private final PositionService positionService;
    private final NotificationService notificationService;
    private final ObjectProperty<Suspicion> suspicionObjectProperty;

    @Autowired
    public AccuseService(PlayerService playerService, CardConverter cardConverter, GameLogicService gameLogicService, PositionService positionService, NotificationService notificationService) {
        this.playerService = playerService;
        this.cardConverter = cardConverter;
        this.gameLogicService = gameLogicService;
        this.positionService = positionService;
        this.notificationService = notificationService;
        this.suspicionObjectProperty = new SimpleObjectProperty<>();
        hookupChangeListeners();
    }

    public void accuse(Suspicion suspicion) {
        suspicionObjectProperty.setValue(suspicion);
    }

    private void hookupChangeListeners() {
        suspicionObjectProperty.addListener((observable, oldValue, newValue) -> {
            if (null != newValue) {
                notificationService.broadcast(new AccusationNotification(newValue));
                gameLogicService.getGameLogic().accuse(newValue);
                positionService.update();
                if (gameLogicService.getGameLogic().isGameEnded()) {
                    gameLogicService.finishTheGame();
                    notificationService.broadcast(new GameEndedNotification());
                    notificationService.close();
                } else {
                    Player currentPlayer = playerService.killPlayer();
                    notificationService.broadcast(getPlayerOutNotification(currentPlayer));
                }
            }
        });
    }

    private Notification getPlayerOutNotification(Player currentPlayer) {
        return new PlayerOutNotification(currentPlayer.getUsername(), cardConverter.convert(currentPlayer.getCards()));
    }

}
