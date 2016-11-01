package me.belakede.thesis.server.game.converter;

import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.internal.game.DefaultPlayer;
import me.belakede.thesis.server.game.domain.Player;
import me.belakede.thesis.server.game.domain.PlayerCard;
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

    public me.belakede.thesis.game.Player convert(Player player) {
        Suspect figurine = player.getFigurine();
        Set<Card> cards = player.getCards().stream().map(cardConverter::convert).collect(Collectors.toSet());
        Boolean alive = player.isAlive();
        return new DefaultPlayer(figurine, cards, alive);
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
