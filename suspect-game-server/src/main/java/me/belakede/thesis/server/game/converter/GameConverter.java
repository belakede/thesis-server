package me.belakede.thesis.server.game.converter;

import me.belakede.thesis.game.Player;
import me.belakede.thesis.game.equipment.BoardType;
import me.belakede.thesis.game.equipment.Case;
import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.internal.game.util.Coordinate;
import me.belakede.thesis.internal.game.util.GameRebuilder;
import me.belakede.thesis.server.game.domain.Game;
import me.belakede.thesis.server.game.domain.Mystery;
import me.belakede.thesis.server.game.domain.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class GameConverter {

    private final PlayerConverter playerConverter;
    private final MysteryConverter mysteryConverter;
    private final PositionConverter positionConverter;

    @Autowired
    public GameConverter(PlayerConverter playerConverter, MysteryConverter mysteryConverter, PositionConverter positionConverter) {
        this.playerConverter = playerConverter;
        this.mysteryConverter = mysteryConverter;
        this.positionConverter = positionConverter;
    }

    public me.belakede.thesis.game.Game convert(Game game) throws IOException {
        BoardType boardType = game.getBoardType();
        Case mystery = mysteryConverter.convert(game.getMystery());
        List<Player> players = game.getPlayers().stream().map(playerConverter::convert).collect(Collectors.toList());
        Optional<Player> currentPlayer = game.getPlayers().stream().filter(p -> p.isCurrent()).map(playerConverter::convert).findFirst();
        Map<Figurine, Coordinate> positions = positionConverter.convert(game.getPositions());

        return GameRebuilder.create()
                .boardType(boardType)
                .mystery(mystery)
                .players(players, currentPlayer.orElse(players.get(0)))
                .positions(positions)
                .build();
    }

    public Game convert(me.belakede.thesis.game.Game game) {
        BoardType boardType = game.getBoard().getBoardType();
        List<Position> positions = positionConverter.convert(game.getPositions());
        List<me.belakede.thesis.server.game.domain.Player> players = new ArrayList<>();
        Player current = game.getCurrentPlayer();
        do {
            players.add(playerConverter.convert(game.getCurrentPlayer()));
            game.next();
        } while (current.equals(game.getCurrentPlayer()));
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setOrdinalNumber(i);
        }
        players.get(0).setCurrent(true);
        boolean gameEnded = game.isGameEnded();
        Mystery mystery = mysteryConverter.convert(game.getMystery());

        Game result = new Game(boardType, gameEnded ? Game.Status.FINISHED : Game.Status.CREATED);
        result.setMystery(mystery);
        result.setPlayers(players);
        result.setPositions(positions);
        return result;
    }

}
