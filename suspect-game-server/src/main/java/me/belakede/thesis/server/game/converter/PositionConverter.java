package me.belakede.thesis.server.game.converter;

import me.belakede.thesis.server.game.domain.Position;
import me.belakede.thesis.server.game.response.Coordinate;
import me.belakede.thesis.server.game.response.FigurineNotification;
import org.springframework.stereotype.Component;

@Component
public class PositionConverter {

    public FigurineNotification convert(Position position) {
        return new FigurineNotification(position.getFigurine(), new Coordinate(position.getRowIndex(), position.getColumnIndex()));
    }

}
