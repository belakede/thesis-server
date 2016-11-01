package me.belakede.thesis.server.game.converter;

import me.belakede.thesis.game.board.Field;
import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.internal.game.util.Coordinate;
import me.belakede.thesis.internal.game.util.Figurines;
import me.belakede.thesis.server.game.domain.Position;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PositionConverter {

    public Map<Figurine, Coordinate> convert(Collection<Position> positions) {
        Map<Figurine, Coordinate> coordinates = new HashMap<>(positions.size());
        positions.forEach(p -> coordinates.put(Figurines.valueOf(p.getFigurine()).get(), convert(p)));
        return coordinates;
    }

    public Set<Position> convert(Map<Figurine, Field> fields) {
        return fields.entrySet().stream()
                .map(e -> new Position(e.getKey().name(), e.getValue().getRow(), e.getValue().getColumn()))
                .collect(Collectors.toSet());
    }

    private Coordinate convert(Position position) {
        return new Coordinate(position.getRowIndex(), position.getColumnIndex());
    }

}
