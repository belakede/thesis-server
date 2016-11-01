package me.belakede.thesis.server.game.exception;

public class MissingUserException extends RuntimeException {

    public MissingUserException(String message) {
        super(message);
    }
}
