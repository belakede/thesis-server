package me.belakede.thesis.server.game.service;

import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.MapChangeListener.Change;
import javafx.collections.ObservableMap;
import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.game.field.Field;
import me.belakede.thesis.server.game.converter.PositionConverter;
import me.belakede.thesis.server.game.domain.Position;
import me.belakede.thesis.server.game.repository.PositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class PositionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PositionService.class);

    private final MapProperty<Figurine, Position> positions = new SimpleMapProperty<>();
    private final GameService gameService;
    private final PositionConverter positionConverter;
    private final PositionRepository positionRepository;
    private final NotificationService notificationService;

    @Autowired
    public PositionService(GameService gameService, PositionConverter positionConverter, PositionRepository positionRepository, NotificationService notificationService) {
        this.gameService = gameService;
        this.positionConverter = positionConverter;
        this.positionRepository = positionRepository;
        this.notificationService = notificationService;
        setPositions(FXCollections.observableHashMap());
        hookupChangeListeners();
    }

    void update() {
        fetchUpdates();
    }

    private void hookupChangeListeners() {
        gameService.runningProperty().addListener((observable, oldValue, newValue) -> {
            MapChangeListener<Figurine, Position> positionMapChangeListener = createFigurinePositionMapChangeListener();
            if (newValue) {
                fetchUpdates();
                getPositions().addListener(positionMapChangeListener);
            } else {
                getPositions().removeListener(positionMapChangeListener);
                positions.clear();
            }
        });
    }

    private MapChangeListener<Figurine, Position> createFigurinePositionMapChangeListener() {
        return (Change<? extends Figurine, ? extends Position> change) -> {
            if (change.wasAdded()) {
                Position position = positionRepository.save(change.getValueAdded());
                LOGGER.debug("{} position has been changed to {}", change.getKey(), change.getValueAdded());
                notificationService.broadcast(positionConverter.convert(position));
            }
        };
    }

    private ObservableMap<Figurine, Position> getPositions() {
        return positions.get();
    }

    private void setPositions(ObservableMap<Figurine, Position> positions) {
        this.positions.set(positions);
    }

    private void fetchUpdates() {
        gameService.getGameLogic().getPositions().forEach((figurine, field) -> {
            LOGGER.info("{} position changed to {}", figurine, field);
            Position position = positions.get(figurine);
            if (position == null || position.getRowIndex() != field.getRow() || position.getColumnIndex() != field.getColumn()) {
                Position newPosition = positionConverter.convert(figurine, field);
                LOGGER.info("The figurine not on board, or it is on a new positions: {}", newPosition);
                newPosition.setGame(gameService.getGameEntity());
                positions.put(figurine, newPosition);
                if (position != null) {
                    LOGGER.info("The figurine already was on the board. So we will delete the old position.");
                    positionRepository.delete(position);
                }
            }
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
}
