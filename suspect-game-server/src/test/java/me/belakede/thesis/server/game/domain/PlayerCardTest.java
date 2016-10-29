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
    public static final class PlayerCardEqualsHashCodeTest extends ExtendedEqualsHashCodeTestCase {

        public PlayerCardEqualsHashCodeTest(String name) {
            super(name, PlayerCard.class, Arrays.asList("player", "card"));
        }

        @Override
        protected PlayerCard createInstance() throws Exception {
            Player player = new Player();
            player.setGame(new Game());
            player.setName("John");
            player.setSuspect(Suspect.PLUM);
            player.setAlive(true);
            PlayerCard playerCard = new PlayerCard();
            playerCard.setPlayer(player);
            playerCard.setCard(Room.BEDROOM.name());
            return playerCard;
        }

        @Override
        protected PlayerCard createNotEqualInstance() throws Exception {
            Player player = new Player();
            player.setGame(new Game());
            player.setName("Jane");
            player.setSuspect(Suspect.SCARLET);
            player.setAlive(false);
            PlayerCard playerCard = new PlayerCard();
            playerCard.setPlayer(player);
            playerCard.setCard(Weapon.LEAD_PIPE.name());
            return playerCard;
        }
    }

    public static final class PlayerCardPojoTest extends PojoClassTestCase {

        public PlayerCardPojoTest(String name) {
            super(name, PlayerCard.class);
        }
    }
}