package me.belakede.thesis.server.chat.configuration;

import me.belakede.thesis.server.chat.exception.MissingSenderException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ChatServerControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({MissingSenderException.class})
    public String handleException(Exception e) {
        return e.getClass() + ": " + e.getMessage();
    }

}