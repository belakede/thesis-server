package me.belakede.thesis.server.game.domain;

import me.belakede.thesis.game.equipment.BoardType;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;
import me.belakede.thesis.junit.ExtendedEqualsHashCodeTestCase;
import me.belakede.thesis.junit.PojoClassTestCase;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.Arrays;

@RunWith(Enclosed.class)
public class PositionTest {

    private static final Game game = new Game(BoardType.ADVANCED, Game.Status.CREATED);

    public static final class PositionEqualsHashCodeTest extends ExtendedEqualsHashCodeTestCase {
        public PositionEqualsHashCodeTest(String name) {
            super(name, Position.class, Arrays.asList("figurine", "game", "rowIndex", "columnIndex"));
        }

        @Override
        protected Position createInstance() throws Exception {
            return new Position(Suspect.MUSTARD.name(), game, 15, 18);
        }

        @Override
        protected Position createNotEqualInstance() throws Exception {
            return new Position(Weapon.LEAD_PIPE.name(), game, 22, 12);
        }
    }

    public static final class PositionPojoTest extends PojoClassTestCase {
        public PositionPojoTest(String name) {
            super(name, Position.class);
        }
    }

}