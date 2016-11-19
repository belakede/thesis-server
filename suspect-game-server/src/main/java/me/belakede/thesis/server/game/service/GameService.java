package me.belakede.thesis.server.game.service;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import me.belakede.thesis.server.game.converter.GameConverter;
import me.belakede.thesis.server.game.domain.Game;
import me.belakede.thesis.server.game.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final ObjectProperty<me.belakede.thesis.game.Game> gameLogic = new SimpleObjectProperty<>();
    private final ObjectProperty<me.belakede.thesis.server.game.domain.Game> gameEntity = new SimpleObjectProperty<>();
    private final BooleanProperty running = new SimpleBooleanProperty();
    private final BooleanProperty available = new SimpleBooleanProperty();
    private final BooleanProperty inProgress = new SimpleBooleanProperty();
    private final GameConverter gameConverter;
    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameConverter gameConverter, GameRepository gameRepository) {
        this.gameConverter = gameConverter;
        this.gameRepository = gameRepository;
        hookupChangeListeners();
    }

    public boolean isRunning() {
        return running.get();
    }

    void setRunning(boolean running) {
        this.running.set(running);
    }

    public BooleanProperty runningProperty() {
        return running;
    }

    boolean isAvailable() {
        return available.get();
    }

    private void setAvailable(boolean available) {
        this.available.set(available);
    }

    BooleanProperty availableProperty() {
        return available;
    }

    public BooleanProperty inProgressProperty() {
        return inProgress;
    }

    me.belakede.thesis.game.Game getGameLogic() {
        return gameLogic.get();
    }

    private void setGameLogic(me.belakede.thesis.game.Game gameLogic) {
        this.gameLogic.set(gameLogic);
    }

    me.belakede.thesis.server.game.domain.Game getGameEntity() {
        return gameEntity.get();
    }

    public void setGameEntity(me.belakede.thesis.server.game.domain.Game gameEntity) {
        this.gameEntity.set(gameEntity);
    }

    void pauseTheGame() {
        if (isRunning() && getGameEntity() != null) {
            setStatus(Game.Status.PAUSED);
            saveAndFlush();
            clearGames();
        }
    }

    void finishTheGame() {
        if (isRunning()) {
            setStatus(Game.Status.FINISHED);
            saveAndFlush();
            clearGames();
        }
    }

    private void hookupChangeListeners() {
        gameLogicProperty().addListener((observable, oldValue, newValue) -> {
            if (!isAvailable() && newValue != null && !isInProgress()) {
                setInProgress(true);
                setGameEntity(gameConverter.convert(newValue));
                getGameEntity().setStatus(Game.Status.IN_PROGRESS);
                gameRepository.save(getGameEntity());
                setAvailable(true);
                setInProgress(false);
            }
        });
        gameEntityProperty().addListener((observable, oldValue, newValue) -> {
            if (!isAvailable() && newValue != null && !isInProgress()) {
                setInProgress(true);
                setGameLogic(gameConverter.convert(newValue));
                getGameEntity().setStatus(Game.Status.IN_PROGRESS);
                gameRepository.save(getGameEntity());
                setAvailable(true);
                setInProgress(false);
            }
        });
    }

    private void setStatus(Game.Status status) {
        getGameEntity().setStatus(status);
    }

    private void saveAndFlush() {
        gameRepository.saveAndFlush(getGameEntity());
    }

    private void clearGames() {
        setGameEntity(null);
        setGameEntity(null);
        setAvailable(false);
        setRunning(false);
    }

    private ObjectProperty<me.belakede.thesis.game.Game> gameLogicProperty() {
        return gameLogic;
    }

    private ObjectProperty<me.belakede.thesis.server.game.domain.Game> gameEntityProperty() {
        return gameEntity;
    }

    private boolean isInProgress() {
        return inProgress.get();
    }

    private void setInProgress(boolean inProgress) {
        this.inProgress.set(inProgress);
    }

}
