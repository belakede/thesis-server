package me.belakede.thesis.server.game.converter;

import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;
import me.belakede.thesis.server.game.domain.Position;
import me.belakede.thesis.server.game.response.Coordinate;
import me.belakede.thesis.server.game.response.FigurineNotification;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

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

}