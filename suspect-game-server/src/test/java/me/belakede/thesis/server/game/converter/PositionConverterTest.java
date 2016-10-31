package me.belakede.thesis.server.game.converter;

import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;
import me.belakede.thesis.internal.game.util.Coordinate;
import me.belakede.thesis.server.game.domain.Game;
import me.belakede.thesis.server.game.domain.Position;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    public void testConvertShouldProduceAPositionSetFromAFigurineCoordinateMap() throws Exception {
        Game game = mock(Game.class);
        Map<Figurine, Coordinate> coordinates = new HashMap<>();
        coordinates.put(Suspect.WHITE, new Coordinate(10, 13));
        coordinates.put(Weapon.REVOLVER, new Coordinate(4, 4));

        Set<Position> actual = testSubject.convert(game, coordinates);

        assertThat(actual.size(), is(2));
        assertThat(actual, containsInAnyOrder(new Position(Suspect.WHITE.name(), game, 10, 13),
                new Position(Weapon.REVOLVER.name(), game, 4, 4)));
    }

    @Test
    public void testConvertShouldProduceAFigurineCoordinateMapFromAPositionSet() throws Exception {
        Game game = mock(Game.class);
        Set<Position> positions = new HashSet<>();
        positions.add(new Position(Suspect.GREEN.name(), game, 10, 12));
        positions.add(new Position(Weapon.LEAD_PIPE.name(), game, 24, 22));

        Map<Figurine, Coordinate> actual = testSubject.convert(positions);

        assertThat(actual.size(), is(2));
        assertThat(actual.get(Suspect.GREEN), is(new Coordinate(10, 12)));
        assertThat(actual.get(Weapon.LEAD_PIPE), is(new Coordinate(24, 22)));
    }
}