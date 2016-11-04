package me.belakede.thesis.server.game.service;

import javafx.beans.property.SimpleMapProperty;
import javafx.collections.MapChangeListener.Change;
import javafx.collections.ObservableMap;
import me.belakede.thesis.server.game.converter.PositionConverter;
import me.belakede.thesis.server.game.domain.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MoveService {

    private final GameLogicService gameLogicService;
    private final PositionConverter positionConverter;
    private final NotificationService notificationService;
    private final ObservableMap<String, Position> positions;

    @Autowired
    public MoveService(GameLogicService gameLogicService, PositionConverter positionConverter, NotificationService notificationService) {
        this.gameLogicService = gameLogicService;
        this.positionConverter = positionConverter;
        this.notificationService = notificationService;
        this.positions = new SimpleMapProperty<>();
        hookupChangeListeners();
    }

    private void move(String user, Position position) {
        // TODO create UserService to handle users
    }

    private void hookupChangeListeners() {
        positions.addListener((Change<? extends String, ? extends Position> change) -> {
            if (change.wasAdded()) {
                notificationService.broadcast(positionConverter.convert(change.getValueAdded()));
            }
        });
    }

}
