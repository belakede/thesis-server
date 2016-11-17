package me.belakede.thesis.server.game.service;

import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Suspicion;
import me.belakede.thesis.server.game.exception.MissingGameException;
import me.belakede.thesis.server.game.response.Coordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.security.Principal;

@Service
public class GameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);

    private final GameLogicService gameLogicService;
    private final JoinService joinService;
    private final RollService rollService;
    private final MoveService moveService;
    private final SuspectService suspectService;
    private final ShowService showService;
    private final AccuseService accuseService;
    private final PlayerService playerService;

    @Autowired
    public GameService(GameLogicService gameLogicService, JoinService joinService, RollService rollService, MoveService moveService, SuspectService suspectService, ShowService showService, AccuseService accuseService, PlayerService playerService) {
        this.gameLogicService = gameLogicService;
        this.joinService = joinService;
        this.rollService = rollService;
        this.moveService = moveService;
        this.suspectService = suspectService;
        this.showService = showService;
        this.accuseService = accuseService;
        this.playerService = playerService;
    }

    public SseEmitter join(Principal principal) {
        if (gameLogicService.gameInProgress()) {
            LOGGER.info("Opening channel for {}", principal.getName());
            SseEmitter emitter = joinService.join(principal.getName());
            LOGGER.info("Emitter created for {}: ", principal.getName(), emitter);
            return emitter;
        }
        throw new MissingGameException("There is no game which you could join!");
    }

    public void roll(Principal principal) {
        if (isCurrentPlayer(principal)) {
            rollService.roll();
        }
    }

    public void move(Principal principal, Coordinate coordinate) {
        if (isCurrentPlayer(principal)) {
            moveService.move(coordinate.getRow(), coordinate.getColumn());
        }
    }

    public void next(Principal principal) {
        if (isCurrentPlayer(principal)) {
            playerService.next();
        }
    }

    public void suspect(Principal principal, Suspicion suspicion) {
        if (isCurrentPlayer(principal)) {
            suspectService.suspect(suspicion);
        }
    }

    public void show(Principal principal, Card card) {
        if (isNextPlayer(principal)) {
            showService.show(card);
        }
    }

    public void accuse(Principal principal, Suspicion suspicion) {
        if (isCurrentPlayer(principal)) {
            accuseService.accuse(suspicion);
        }
    }

    public void exit(Principal principal) {
        if (isCurrentPlayer(principal)) {
            joinService.exit(principal.getName());
        }
    }

    private boolean isCurrentPlayer(Principal principal) {
        return principal.getName().equals(playerService.getCurrentPlayer().getUsername());
    }

    private boolean isNextPlayer(Principal principal) {
        return principal.getName().equals(playerService.getNextPlayer().getUsername());
    }

}
