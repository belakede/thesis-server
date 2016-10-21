package me.belakede.thesis.server.auth.exception;

public class MissingUserException extends Exception {

    public MissingUserException() {
    }

    public MissingUserException(String message) {
        super(message);
    }

    public MissingUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
