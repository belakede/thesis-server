package me.belakede.thesis.server.game.converter;

import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.server.game.domain.Position;
import me.belakede.thesis.server.game.response.Coordinate;
import me.belakede.thesis.server.game.response.FigurineNotification;
import org.junit.Before;
import org.junit.Test;

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

}