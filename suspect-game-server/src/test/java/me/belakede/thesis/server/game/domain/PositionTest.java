package me.belakede.thesis.server.game.domain;

import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.junit.ExtendedEqualsHashCodeTestCase;
import me.belakede.thesis.junit.PojoClassTestCase;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.Arrays;

@RunWith(Enclosed.class)
public class PositionTest {

    public static final class PositionEqualsHashCodeTest extends ExtendedEqualsHashCodeTestCase {

        public PositionEqualsHashCodeTest(String name) {
            super(name, Position.class, Arrays.asList("player", "row", "column"));
        }

        @Override
        protected Position createInstance() throws Exception {
            Player player = new Player();
            player.setGame(new Game());
            player.setName("John");
            player.setSuspect(Suspect.PLUM);
            player.setAlive(true);
            Position position = new Position();
            position.setPlayer(player);
            position.setRow(1);
            position.setColumn(5);
            return position;
        }

        @Override
        protected Position createNotEqualInstance() throws Exception {
            Player player = new Player();
            player.setGame(new Game());
            player.setName("Jane");
            player.setSuspect(Suspect.SCARLET);
            player.setAlive(false);
            Position position = new Position();
            position.setPlayer(player);
            position.setRow(5);
            position.setColumn(1);
            return position;
        }
    }

    public static final class PositionPojoTest extends PojoClassTestCase {

        public PositionPojoTest(String name) {
            super(name, Position.class);
        }
    }

}