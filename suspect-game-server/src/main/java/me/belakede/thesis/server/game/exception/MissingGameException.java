package me.belakede.thesis.server.game.exception;

public class MissingGameException extends RuntimeException {
    public MissingGameException(String message) {
        super(message);
    }
}
