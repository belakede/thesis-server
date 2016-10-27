package me.belakede.thesis.server.auth.configuration;

import me.belakede.thesis.server.auth.exception.MissingUserException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class AuthServerControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({MissingUserException.class})
    public String handleException(Exception e) {
        return e.getClass() + ": " + e.getMessage();
    }

}