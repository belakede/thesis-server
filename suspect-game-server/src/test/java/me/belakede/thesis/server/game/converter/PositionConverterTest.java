package me.belakede.thesis.server.game.converter;

import me.belakede.thesis.game.board.Field;
import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;
import me.belakede.thesis.internal.game.board.FieldFactory;
import me.belakede.thesis.server.game.domain.Game;
import me.belakede.thesis.server.game.domain.Position;
import me.belakede.thesis.server.game.response.Coordinate;
import me.belakede.thesis.server.game.response.FigurineNotification;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class PositionConverterTest {

    private PositionConverter testSubject;

    @Before
    public void setUp() throws Exception {
        testSubject = new PositionConverter();
    }

    @Test
    public void testConvertShouldProduceAFigurineNotificationFromAPosition() {
        Position position = new Position(Suspect.GREEN.name(), 10, 12);

        FigurineNotification actual = testSubject.convert(position);

        assertThat(actual.getFigurine(), is(Suspect.GREEN));
        assertThat(actual.getPosition(), is(new Coordinate(10, 12)));
    }

    @Test
    public void testConvertShouldProduceACollectionOfFigurineNotificationFromAPositionList() {
        List<Position> positions = Arrays.asList(
                new Position(Suspect.GREEN.name(), 10, 12),
                new Position(Suspect.MUSTARD.name(), 26, 3),
                new Position(Weapon.CANDLESTICK.name(), 4, 16)
        );

        Collection<FigurineNotification> actual = testSubject.convert(positions);

        assertThat(actual.size(), is(3));
        assertThat(actual, containsInAnyOrder(
                new FigurineNotification(Suspect.GREEN, new Coordinate(10, 12)),
                new FigurineNotification(Suspect.MUSTARD, new Coordinate(26, 3)),
                new FigurineNotification(Weapon.CANDLESTICK, new Coordinate(4, 16))
        ));
    }

    @Test
    public void testConvertShouldProduceAPositionSetFromAFigurineFieldMap() {
        Game game = mock(Game.class);
        Map<Figurine, Field> coordinates = new HashMap<>();
        coordinates.put(Suspect.WHITE, FieldFactory.getFieldBySymbol(10, 13, 'S'));
        coordinates.put(Weapon.REVOLVER, FieldFactory.getFieldBySymbol(4, 4, 'R'));

        List<Position> actual = testSubject.convert(coordinates);
        actual.forEach(p -> p.setGame(game));

        assertThat(actual.size(), is(2));
        assertThat(actual, containsInAnyOrder(new Position(Suspect.WHITE.name(), game, 10, 13),
                new Position(Weapon.REVOLVER.name(), game, 4, 4)));
    }

}