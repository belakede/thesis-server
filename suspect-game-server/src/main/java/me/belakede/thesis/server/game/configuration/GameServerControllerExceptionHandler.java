package me.belakede.thesis.server.game.configuration;

import me.belakede.thesis.server.game.exception.InvalidPlayerConfiguration;
import me.belakede.thesis.server.game.exception.MissingBoardException;
import me.belakede.thesis.server.game.exception.MissingGameException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class GameServerControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({MissingBoardException.class, MissingGameException.class, InvalidPlayerConfiguration.class})
    public String handleException(Exception e) {
        return e.getClass() + ": " + e.getMessage();
    }

}