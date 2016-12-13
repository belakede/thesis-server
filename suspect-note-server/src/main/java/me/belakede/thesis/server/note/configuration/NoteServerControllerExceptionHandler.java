package me.belakede.thesis.server.note.configuration;

import me.belakede.thesis.server.note.exception.MissingAuthorException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class NoteServerControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({MissingAuthorException.class})
    public String handleException(Exception e) {
        return e.getClass() + ": " + e.getMessage();
    }

}