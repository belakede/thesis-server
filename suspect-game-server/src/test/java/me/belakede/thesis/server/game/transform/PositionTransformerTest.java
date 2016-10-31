package me.belakede.thesis.server.game.transform;

import me.belakede.thesis.game.board.Field;
import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;
import me.belakede.thesis.internal.game.board.FieldFactory;
import me.belakede.thesis.internal.game.util.Coordinate;
import me.belakede.thesis.server.game.domain.Player;
import me.belakede.thesis.server.game.domain.Position;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PositionTransformerTest {

    private PositionTransformer testSubject;

    @Before
    public void setUp() throws Exception {
        testSubject = new PositionTransformer();
    }

    @Test
    public void testTransformShouldTransformAFieldIntoAPosition() {
        int row = 14;
        int column = 10;
        Field field = FieldFactory.getFieldBySymbol(row, column, 'S');
        Position actual = testSubject.transform(field);
        assertThat(actual.getRow(), is(row));
        assertThat(actual.getColumn(), is(column));
    }

    @Test
    public void testTransformShouldTransformAPositionIntoACoordinate() {
        int row = 13;
        int column = 0;
        Coordinate actual = testSubject.transform(createPosition(row, column));
        assertThat(actual.getRow(), is(row));
        assertThat(actual.getColumn(), is(column));
    }

    @Test
    public void testTransformShouldTransformAPackOfPlayerIntoAFigurineCoordinatePositionMap() {
        List<Player> players = new ArrayList<>();
        players.add(createPlayer(Suspect.PLUM, 10, 11));
        players.add(createPlayer(Suspect.GREEN, 2, 1));
        players.add(createPlayer(Suspect.SCARLET, 7, 2));
        players.add(createPlayer(Suspect.WHITE, 17, 14));

        Map<Figurine, Coordinate> actual = testSubject.transform(players);

        assertThat(actual.size(), is(4));
        assertThat(actual.get(Suspect.PLUM), is(new Coordinate(10, 11)));
        assertThat(actual.get(Suspect.GREEN), is(new Coordinate(2, 1)));
        assertThat(actual.get(Suspect.SCARLET), is(new Coordinate(7, 2)));
        assertThat(actual.get(Suspect.WHITE), is(new Coordinate(17, 14)));
    }

    @Test
    public void testTransformShouldTransformAFigurineFieldMapIntoASuspectPositionMap() {
        Map<Figurine, Field> fieldMap = new HashMap<>();
        fieldMap.put(Suspect.MUSTARD, FieldFactory.getFieldBySymbol(10, 15, 'S'));
        fieldMap.put(Weapon.REVOLVER, FieldFactory.getFieldBySymbol(4, 4, 'R'));
        fieldMap.put(Suspect.PEACOCK, FieldFactory.getFieldBySymbol(12, 21, 'S'));
        fieldMap.put(Weapon.ROPE, FieldFactory.getFieldBySymbol(24, 8, 'R'));

        Map<Suspect, Position> actual = testSubject.transform(fieldMap);

        assertThat(actual.size(), is(2));
        assertThat(actual.get(Suspect.MUSTARD), is(createPosition(10, 15)));
        assertThat(actual.get(Suspect.PEACOCK), is(createPosition(12, 21)));
    }

    private Player createPlayer(Suspect suspect, int row, int column) {
        Position position = createPosition(row, column);
        Player player = new Player();
        player.setSuspect(suspect);
        player.setPosition(position);
        return player;
    }

    private Position createPosition(int row, int column) {
        Position position = new Position();
        position.setRow(row);
        position.setColumn(column);
        return position;
    }


}