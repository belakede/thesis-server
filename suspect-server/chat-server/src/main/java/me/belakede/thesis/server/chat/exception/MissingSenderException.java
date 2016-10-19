package me.belakede.thesis.server.chat.exception;


public class MissingSenderException extends Exception {

    public MissingSenderException() {
        super();
    }

    public MissingSenderException(String message) {
        super(message);
    }
}
