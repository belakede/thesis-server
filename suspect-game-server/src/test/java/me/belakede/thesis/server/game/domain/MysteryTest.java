package me.belakede.thesis.server.game.domain;

import me.belakede.thesis.game.board.BoardType;
import me.belakede.thesis.game.equipment.Room;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;
import me.belakede.thesis.junit.ExtendedEqualsHashCodeTestCase;
import me.belakede.thesis.junit.PojoClassTestCase;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.Arrays;

@RunWith(Enclosed.class)
public class MysteryTest {

    private static final Game game = new Game(BoardType.ADVANCED, Game.Status.IN_PROGRESS);

    public static final class MysteryEqualsHashCodeTest extends ExtendedEqualsHashCodeTestCase {
        public MysteryEqualsHashCodeTest(String name) {
            super(name, Mystery.class, Arrays.asList("game", "suspect", "room", "weapon"));
        }

        @Override
        protected Mystery createInstance() throws Exception {
            return new Mystery(game, Suspect.WHITE, Room.HALL, Weapon.ROPE);
        }

        @Override
        protected Mystery createNotEqualInstance() throws Exception {
            return new Mystery(game, Suspect.PEACOCK, Room.BALLROOM, Weapon.KNIFE);
        }
    }

    public static final class MysteryPojoTest extends PojoClassTestCase {
        public MysteryPojoTest(String name) {
            super(name, Mystery.class);
        }
    }

}