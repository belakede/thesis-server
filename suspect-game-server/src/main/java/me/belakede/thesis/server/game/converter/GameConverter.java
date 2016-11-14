package me.belakede.thesis.server.game.converter;

import me.belakede.thesis.game.Player;
import me.belakede.thesis.game.equipment.BoardType;
import me.belakede.thesis.game.equipment.Case;
import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.internal.game.util.Coordinate;
import me.belakede.thesis.internal.game.util.GameRebuilder;
import me.belakede.thesis.server.game.domain.Game;
import me.belakede.thesis.server.game.domain.Mystery;
import me.belakede.thesis.server.game.domain.Position;
import me.belakede.thesis.server.game.exception.MissingBoardException;
import me.belakede.thesis.server.game.response.BoardStatus;
import me.belakede.thesis.server.game.response.FigurineNotification;
import me.belakede.thesis.server.game.response.GameStatusNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
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

    public Game convert(me.belakede.thesis.game.Game game) {
        BoardType boardType = game.getBoard().getBoardType();
        List<Position> positions = positionConverter.convert(game.getPositions());
        List<me.belakede.thesis.server.game.domain.Player> players = new ArrayList<>();
        Player current = game.getCurrentPlayer();
        do {
            players.add(playerConverter.convert(game.getCurrentPlayer()));
            game.next();
        } while (!current.equals(game.getCurrentPlayer()));
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setOrdinalNumber(i);
        }
        players.get(0).setCurrent(true);
        boolean gameEnded = game.isGameEnded();
        Mystery mystery = mysteryConverter.convert(game.getMystery());

        Game result = new Game(boardType, gameEnded ? Game.Status.FINISHED : Game.Status.CREATED);
        result.setMystery(mystery);
        mystery.setGame(result);
        result.setPlayers(players);
        players.forEach(p -> p.setGame(result));
        result.setPositions(positions);
        positions.forEach(p -> p.setGame(result));
        return result;
    }

    public GameStatusNotification convert(Game game, Collection<me.belakede.thesis.server.game.domain.Player> players) {
        List<FigurineNotification> positions = game.getPositions().stream()
                .map(p -> new FigurineNotification(p.getFigurine(), new me.belakede.thesis.server.game.response.Coordinate(p.getRowIndex(), p.getColumnIndex())))
                .collect(Collectors.toList());
        BoardStatus boardStatus = new BoardStatus(game.getBoardType(), positions);
        Map<Suspect, String> suspectUsernameMap = new HashMap<>();
        players.forEach(p -> suspectUsernameMap.put(p.getFigurine(), p.getUsername()));
        return new GameStatusNotification(boardStatus, suspectUsernameMap);
    }

    public me.belakede.thesis.game.Game convert(Game game) {
        BoardType boardType = game.getBoardType();
        Case mystery = mysteryConverter.convert(game.getMystery());
        List<me.belakede.thesis.server.game.domain.Player> players = game.getPlayers();
        Collections.sort(players, (o1, o2) -> o1.getOrdinalNumber() - o2.getOrdinalNumber());
        List<Player> logicPlayers = players.stream().map(playerConverter::convertToPlayer).collect(Collectors.toList());
        Map<Figurine, Coordinate> positions = positionConverter.convertToMap(game.getPositions());
        try {
            return GameRebuilder.create().boardType(boardType).mystery(mystery).players(logicPlayers, logicPlayers.get(0)).positions(positions).build();
        } catch (IOException e) {
            throw new MissingBoardException("Board not found: " + boardType);
        }
    }

}
