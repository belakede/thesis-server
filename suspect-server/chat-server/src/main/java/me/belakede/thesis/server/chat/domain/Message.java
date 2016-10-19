package me.belakede.thesis.server.chat.domain;

import java.time.LocalDateTime;

public class Message {

    private String sender;
    private LocalDateTime time;
    private String message;

    public Message() {
    }

    public Message(String sender, String message) {
        this.sender = sender;
        this.message = message;
        this.time = LocalDateTime.now();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message1 = (Message) o;

        return sender != null ? sender.equals(message1.sender) : message1.sender == null
                && (time != null ? time.equals(message1.time) : message1.time == null
                && (message != null ? message.equals(message1.message) : message1.message == null));

    }

    @Override
    public int hashCode() {
        int result = sender != null ? sender.hashCode() : 0;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
