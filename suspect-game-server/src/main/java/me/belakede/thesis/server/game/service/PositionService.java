package me.belakede.thesis.server.game.service;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.game.field.Field;
import me.belakede.thesis.internal.game.util.Figurines;
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
    private final BooleanProperty initialized = new SimpleBooleanProperty();
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
        setInitialized(false);
        setPositions(FXCollections.observableHashMap());
        hookupChangeListeners();
    }

    void update() {
        fetchUpdates();
    }

    public boolean isInitialized() {
        return initialized.get();
    }

    public void setInitialized(boolean initialized) {
        this.initialized.set(initialized);
    }

    public BooleanProperty initializedProperty() {
        return initialized;
    }

    public MapProperty<Figurine, Position> positionsProperty() {
        return positions;
    }

    private void hookupChangeListeners() {
        gameService.runningProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                fetchUpdates();
            } else {
                positionsProperty().clear();
            }
            setInitialized(newValue);
        });
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
            Position position = changePosition(figurine, field);
            positions.put(figurine, position);
            gameService.updatePositions(positions.values());
        });
        if (positions.size() != Figurines.values().size()) {
            LOGGER.error("Some figurines are missing: {} -> {}", Figurines.values(), positions.keySet());
        }
    }

    private Position changePosition(Figurine figurine, Field field) {
        Position position;
        position = positions.get(figurine);
        if (position == null) {
            position = new Position(figurine.name(), gameService.getGameEntity(), field.getRow(), field.getColumn());
        } else if (!position.isIdentical(field)) {
            position.setRowIndex(field.getRow());
            position.setColumnIndex(field.getColumn());
            notificationService.broadcast(positionConverter.convert(position));
        }
        return positionRepository.save(position);
    }
}
