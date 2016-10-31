package me.belakede.thesis.server.game.domain;

import me.belakede.thesis.game.board.BoardType;
import me.belakede.thesis.junit.ExtendedEqualsHashCodeTestCase;
import me.belakede.thesis.junit.PojoClassTestCase;
import me.belakede.thesis.time.TimeMachine;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.Arrays;

@RunWith(Enclosed.class)
public class GameTest {

    @BeforeClass
    public static void setUp() throws Exception {
        TimeMachine.useFixedClockAt(LocalDateTime.now());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        TimeMachine.useSystemDefaultZoneClock();
    }

    public static final class GameEqualsHashCodeTest extends ExtendedEqualsHashCodeTestCase {

        public GameEqualsHashCodeTest(String name) {
            super(name, Game.class, Arrays.asList("room", "created", "boardType", "status"));
        }

        @Override
        protected Game createInstance() throws Exception {
            return new Game("test-room", TimeMachine.now(), BoardType.DEFAULT, Game.Status.CREATED);
        }

        @Override
        protected Game createNotEqualInstance() throws Exception {
            return new Game(BoardType.DEFAULT, Game.Status.CREATED);
        }
    }

    public static final class GamePojoTest extends PojoClassTestCase {

        public GamePojoTest(String name) {
            super(name, Game.class);
        }
    }

}