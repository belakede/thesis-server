package me.belakede.thesis.server.game.domain;

import me.belakede.thesis.game.equipment.Room;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;
import me.belakede.thesis.junit.ExtendedEqualsHashCodeTestCase;
import me.belakede.thesis.junit.PojoClassTestCase;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.Arrays;

@RunWith(Enclosed.class)
public class PlayerCardTest {

    private static final Player player = new Player("admin", Suspect.MUSTARD, 1);

    public static final class PlayerCardEqualsHashCodeTest extends ExtendedEqualsHashCodeTestCase {

        public PlayerCardEqualsHashCodeTest(String name) {
            super(name, Player.class, Arrays.asList("player", "card"));
        }

        @Override
        protected PlayerCard createInstance() throws Exception {
            return new PlayerCard(player, Room.BATHROOM.name());
        }

        @Override
        protected PlayerCard createNotEqualInstance() throws Exception {
            return new PlayerCard(player, Weapon.KNIFE.name());
        }
    }

    public static final class PlayerCardPojoTest extends PojoClassTestCase {

        public PlayerCardPojoTest(String name) {
            super(name, Player.class);
        }
    }

}