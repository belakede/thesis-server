package me.belakede.thesis.server.auth.exception;

public class MissingUserException extends RuntimeException {

    public MissingUserException(String message) {
        super(message);
    }

}
