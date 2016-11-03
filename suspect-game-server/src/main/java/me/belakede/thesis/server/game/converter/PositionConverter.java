package me.belakede.thesis.server.game.converter;

import me.belakede.thesis.game.board.Field;
import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.internal.game.util.Figurines;
import me.belakede.thesis.server.game.domain.Position;
import me.belakede.thesis.server.game.response.Coordinate;
import me.belakede.thesis.server.game.response.FigurineNotification;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PositionConverter {

    public FigurineNotification convert(Position position) {
        return new FigurineNotification(position.getFigurine(), new Coordinate(position.getRowIndex(), position.getColumnIndex()));
    }

    public Collection<FigurineNotification> convert(Collection<Position> positions) {
        return positions.stream().map(this::convert).collect(Collectors.toList());
    }

    public List<Position> convert(Map<Figurine, Field> fields) {
        return fields.entrySet().stream()
                .map(e -> new Position(e.getKey().name(), e.getValue().getRow(), e.getValue().getColumn()))
                .collect(Collectors.toList());
    }

    public Map<Figurine, me.belakede.thesis.internal.game.util.Coordinate> convertToMap(Collection<Position> positions) {
        Map<Figurine, me.belakede.thesis.internal.game.util.Coordinate> coordinates = new HashMap<>(positions.size());
        positions.forEach(p -> coordinates.put(Figurines.valueOf(p.getFigurine()).get(), convertToCoordinate(p)));
        return coordinates;
    }

    private me.belakede.thesis.internal.game.util.Coordinate convertToCoordinate(Position position) {
        return new me.belakede.thesis.internal.game.util.Coordinate(position.getRowIndex(), position.getColumnIndex());
    }

}
