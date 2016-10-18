package me.belakede.thesis.server.chat.exception;

public class MissingRoomException extends Exception {

    public MissingRoomException() {
    }

    public MissingRoomException(String message) {
        super(message);
    }
}
