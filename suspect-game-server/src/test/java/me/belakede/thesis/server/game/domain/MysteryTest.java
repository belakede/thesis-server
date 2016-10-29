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

    public static final class MysteryEqualsHashCodeTest extends ExtendedEqualsHashCodeTestCase {

        public MysteryEqualsHashCodeTest(String name) {
            super(name, Mystery.class, Arrays.asList("game", "suspect", "room", "weapon"));
        }

        @Override
        protected Mystery createInstance() throws Exception {
            Game game = new Game();
            game.setRoomId("roomId");
            game.setStatus(GameStatus.CREATED);
            game.setBoardType(BoardType.DEFAULT);
            Mystery mystery = new Mystery();
            mystery.setGame(game);
            mystery.setSuspect(Suspect.MUSTARD);
            mystery.setRoom(Room.BILLIARD_ROOM);
            mystery.setWeapon(Weapon.CANDLESTICK);
            return mystery;
        }

        @Override
        protected Mystery createNotEqualInstance() throws Exception {
            Game game = new Game();
            game.setRoomId("otherRoomId");
            game.setStatus(GameStatus.FINISHED);
            game.setBoardType(BoardType.ADVANCED);
            Mystery mystery = new Mystery();
            mystery.setGame(new Game());
            mystery.setSuspect(Suspect.PEACOCK);
            mystery.setRoom(Room.HALL);
            mystery.setWeapon(Weapon.ROPE);
            return mystery;
        }
    }

    public static final class MysteryPojoTest extends PojoClassTestCase {

        public MysteryPojoTest(String name) {
            super(name, Mystery.class);
        }
    }

}