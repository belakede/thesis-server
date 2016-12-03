package me.belakede.thesis.server.game.service;

import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Suspicion;
import me.belakede.thesis.server.game.response.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.security.Principal;
import java.util.Optional;
import java.util.Timer;

@Service
public class GameManager {

    private final ExitService exitService;
    private final JoinService joinService;
    private final RollService rollService;
    private final MoveService moveService;
    private final ShowService showService;
    private final NextService nextService;
    private final AccuseService accuseService;
    private final PlayerService playerService;
    private final SuspectService suspectService;

    @Autowired
    public GameManager(ExitService exitService, JoinService joinService, RollService rollService, MoveService moveService, ShowService showService,
                       NextService nextService, AccuseService accuseService, PlayerService playerService, SuspectService suspectService, HeartbeatService heartbeatService,
                       @Value("${heartbeat.delay}") Integer heartBeatDelay, @Value("${heartbeat.period}") Integer heartBeatPeriod) {
        this.exitService = exitService;
        this.joinService = joinService;
        this.rollService = rollService;
        this.moveService = moveService;
        this.showService = showService;
        this.nextService = nextService;
        this.accuseService = accuseService;
        this.playerService = playerService;
        this.suspectService = suspectService;
        startHeartBeatService(heartbeatService, heartBeatDelay, heartBeatPeriod);
    }

    private void startHeartBeatService(HeartbeatService heartbeatService, Integer heartBeatDelay, Integer heartBeatPeriod) {
        Timer timer = new Timer();
        timer.schedule(heartbeatService, heartBeatDelay, heartBeatPeriod);
    }

    public SseEmitter join(Principal principal) {
        return joinService.join(principal.getName());
    }

    public void exit(Principal principal) {
        if (isValidPlayer(principal)) {
            exitService.exit();
        }
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
            nextService.next();
        }
    }

    public void suspect(Principal principal, Suspicion suspicion) {
        if (isCurrentPlayer(principal)) {
            suspectService.suspect(suspicion);
        }
    }

    public void show(Principal principal, Optional<Card> card) {
        if (isNextPlayer(principal)) {
            showService.show(card.orElse(null));
        }

    }

    public void accuse(Principal principal, Suspicion suspicion) {
        if (isCurrentPlayer(principal)) {
            accuseService.accuse(suspicion);
        }
    }

    private boolean isValidPlayer(Principal principal) {
        return playerService.isValidPlayer(principal.getName());
    }

    private boolean isCurrentPlayer(Principal principal) {
        return principal.getName().equals(playerService.getCurrentPlayer().getUsername());
    }

    private boolean isNextPlayer(Principal principal) {
        return principal.getName().equals(playerService.getNextPlayer().getUsername());
    }

}
