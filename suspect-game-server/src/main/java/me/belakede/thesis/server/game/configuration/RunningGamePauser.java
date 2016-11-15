package me.belakede.thesis.server.game.configuration;

import me.belakede.thesis.server.game.domain.Game;
import me.belakede.thesis.server.game.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
public class RunningGamePauser {

    private static final Logger LOGGER = LoggerFactory.getLogger(RunningGamePauser.class);
    private final GameRepository gameRepository;

    @Autowired
    public RunningGamePauser(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @PostConstruct
    public void initIt() throws Exception {
        List<Game> runningGames = gameRepository.findByStatus(Game.Status.IN_PROGRESS);
        LOGGER.info("Running games: {}", runningGames);
        runningGames.forEach(game -> game.setStatus(Game.Status.PAUSED));
        gameRepository.save(runningGames);
        LOGGER.info("Set running games status to PAUSED");
    }

}
