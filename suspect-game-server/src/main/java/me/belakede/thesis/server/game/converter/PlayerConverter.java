package me.belakede.thesis.server.game.converter;

import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.server.game.domain.Player;
import me.belakede.thesis.server.game.domain.PlayerCard;
import me.belakede.thesis.server.game.response.PlayerStatusNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PlayerConverter {

    private final CardConverter cardConverter;

    @Autowired
    public PlayerConverter(CardConverter cardConverter) {
        this.cardConverter = cardConverter;
    }

    public PlayerStatusNotification convert(Player player) {
        Suspect figurine = player.getFigurine();
        Set<String> cards = player.getCards().stream().map(pc -> pc.getCard()).collect(Collectors.toSet());
        Boolean alive = player.isAlive();
        return new PlayerStatusNotification(figurine, cards, alive);
    }

    public Player convert(me.belakede.thesis.game.Player player) {
        Suspect figurine = player.getFigurine();
        Set<PlayerCard> cards = player.getCards().stream().map(cardConverter::convert).collect(Collectors.toSet());
        Player result = new Player(figurine, cards);
        result.setAlive(!player.hasBeenMadeGroundlessAccusation());
        result.getCards().forEach(c -> c.setPlayer(result));
        return result;
    }

}
