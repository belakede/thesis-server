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
            super(name, Game.class, Arrays.asList("roomId", "created", "boardType", "status"));
        }

        @Override
        protected Game createInstance() throws Exception {
            Game game = new Game();
            game.setBoardType(BoardType.DEFAULT);
            game.setCreated(TimeMachine.now());
            game.setRoomId("roomId");
            game.setStatus(GameStatus.IN_PROGRESS);
            return game;
        }

        @Override
        protected Game createNotEqualInstance() throws Exception {
            Game game = new Game();
            game.setBoardType(BoardType.ADVANCED);
            game.setCreated(TimeMachine.now());
            game.setRoomId("otherRoomId");
            game.setStatus(GameStatus.PAUSED);
            return game;
        }
    }

    public static final class GamePojoTest extends PojoClassTestCase {

        public GamePojoTest(String name) {
            super(name, Game.class);
        }
    }


}