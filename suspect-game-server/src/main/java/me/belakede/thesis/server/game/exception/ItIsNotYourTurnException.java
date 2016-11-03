package me.belakede.thesis.server.game.exception;

public class ItIsNotYourTurnException extends RuntimeException {
    public ItIsNotYourTurnException(String message) {
        super(message);
    }
}
