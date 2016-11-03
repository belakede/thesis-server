package me.belakede.thesis.server.game.domain;

import me.belakede.thesis.game.equipment.BoardType;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.junit.ExtendedEqualsHashCodeTestCase;
import me.belakede.thesis.junit.PojoClassTestCase;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.Arrays;

@RunWith(Enclosed.class)
public class PlayerTest {

    private static final Game game = new Game(BoardType.ADVANCED, Game.Status.PAUSED);

    public static final class PlayerEqualsHashCodeTest extends ExtendedEqualsHashCodeTestCase {
        public PlayerEqualsHashCodeTest(String name) {
            super(name, Player.class, Arrays.asList("game", "username"));
        }

        @Override
        protected Player createInstance() throws Exception {
            return new Player(game, "username", Suspect.PEACOCK, 1);
        }

        @Override
        protected Player createNotEqualInstance() throws Exception {
            return new Player(game, "otheruser", Suspect.GREEN, 2);
        }
    }

    public static final class PlayerPojoTest extends PojoClassTestCase {
        public PlayerPojoTest(String name) {
            super(name, Player.class);
        }
    }
}