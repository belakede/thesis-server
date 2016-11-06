package me.belakede.thesis.server.game.service;

import javafx.beans.property.SimpleMapProperty;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import me.belakede.thesis.game.board.Field;
import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.server.game.converter.PositionConverter;
import me.belakede.thesis.server.game.domain.Position;
import me.belakede.thesis.server.game.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PositionService {

    private final GameLogicService gameLogicService;
    private final PositionConverter positionConverter;
    private final PositionRepository positionRepository;
    private final NotificationService notificationService;
    private final ObservableMap<Figurine, Position> positions;

    @Autowired
    public PositionService(GameLogicService gameLogicService, PositionConverter positionConverter, PositionRepository positionRepository, NotificationService notificationService) {
        this.gameLogicService = gameLogicService;
        this.positionConverter = positionConverter;
        this.positionRepository = positionRepository;
        this.notificationService = notificationService;
        this.positions = new SimpleMapProperty<>();
        hookupChangeListeners();
    }

    public void update() {
        fetchUpdates();
    }

    private void fetchUpdates() {
        gameLogicService.getGameLogic().getPositions().forEach((figurine, field) -> {
            Position position = positions.containsKey(figurine)
                    ? changePosition(figurine, field)
                    : positionConverter.convert(figurine, field);
            positions.put(figurine, position);
        });
    }

    private Position changePosition(Figurine figurine, Field field) {
        Position position;
        position = positions.get(figurine);
        if (!position.isIdentical(field)) {
            position.setRowIndex(field.getRow());
            position.setColumnIndex(field.getColumn());
        }
        return position;
    }

    private void hookupChangeListeners() {
        positions.addListener((MapChangeListener.Change<? extends Figurine, ? extends Position> change) -> {
            if (change.wasAdded()) {
                Position position = change.getValueAdded();
                positionRepository.save(position);
                notificationService.broadcast(positionConverter.convert(position));
            }
        });
    }

}
