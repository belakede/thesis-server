package me.belakede.thesis.server.game.exception;

public class GameIsAlreadyRunningException extends RuntimeException {

    public GameIsAlreadyRunningException(String message) {
        super(message);
    }
}
