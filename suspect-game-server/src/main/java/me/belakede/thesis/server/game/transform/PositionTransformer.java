package me.belakede.thesis.server.game.transform;

import me.belakede.thesis.game.board.Field;
import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.internal.game.util.Coordinate;
import me.belakede.thesis.server.game.domain.Player;
import me.belakede.thesis.server.game.domain.Position;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PositionTransformer {

    public Map<Figurine, Coordinate> transform(List<Player> players) {
        Map<Figurine, Coordinate> coordinates = new HashMap<>();
        players.forEach(p -> coordinates.put(p.getSuspect(), transform(p.getPosition())));
        return coordinates;
    }

    public Map<Suspect, Position> transform(Map<Figurine, Field> positions) {
        Map<Suspect, Position> result = new HashMap<>();
        List<Figurine> suspects = Arrays.asList(Suspect.values());
        positions.entrySet().stream()
                .filter(e -> suspects.contains(e.getKey()))
                .forEach(e -> result.put((Suspect) e.getKey(), transform(e.getValue())));
        return result;
    }

    public Position transform(Field field) {
        Position position = new Position();
        position.setRow(field.getRow());
        position.setColumn(field.getColumn());
        return position;
    }

    public Coordinate transform(Position position) {
        return new Coordinate(position.getRow(), position.getColumn());
    }

}
