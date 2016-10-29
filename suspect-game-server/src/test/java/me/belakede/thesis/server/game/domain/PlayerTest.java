package me.belakede.thesis.server.game.domain;

import me.belakede.thesis.game.board.BoardType;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.junit.ExtendedEqualsHashCodeTestCase;
import me.belakede.thesis.junit.PojoClassTestCase;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.Arrays;

@RunWith(Enclosed.class)
public class PlayerTest {

    public static final class PlayerEqualsHashCodeTest extends ExtendedEqualsHashCodeTestCase {

        public PlayerEqualsHashCodeTest(String name) {
            super(name, Player.class, Arrays.asList("game", "name", "character", "alive"));
        }

        @Override
        protected Player createInstance() throws Exception {
            Game game = new Game();
            game.setRoomId("roomId");
            game.setStatus(GameStatus.PAUSED);
            game.setBoardType(BoardType.ADVANCED);
            Player player = new Player();
            player.setGame(game);
            player.setName("John");
            player.setCharacter(Suspect.PLUM);
            player.setAlive(true);
            return player;
        }

        @Override
        protected Player createNotEqualInstance() throws Exception {
            Game game = new Game();
            game.setRoomId("otherRoomId");
            game.setStatus(GameStatus.IN_PROGRESS);
            game.setBoardType(BoardType.DEFAULT);
            Player player = new Player();
            player.setGame(game);
            player.setName("Jane");
            player.setCharacter(Suspect.SCARLET);
            player.setAlive(false);
            return player;
        }
    }

    public static final class PlayerPojoTest extends PojoClassTestCase {

        public PlayerPojoTest(String name) {
            super(name, Player.class);
        }
    }

}