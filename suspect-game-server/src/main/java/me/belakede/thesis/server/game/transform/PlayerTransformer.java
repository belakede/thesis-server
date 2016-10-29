package me.belakede.thesis.server.game.transform;

import me.belakede.thesis.game.Player;
import me.belakede.thesis.internal.game.DefaultPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlayerTransformer {

    private final CardTransformer cardTransformer;

    @Autowired
    public PlayerTransformer(CardTransformer cardTransformer) {
        this.cardTransformer = cardTransformer;
    }

    public Set<Player> transform(Set<me.belakede.thesis.server.game.domain.Player> players) {
        return players.stream().map(this::transform).collect(Collectors.toSet());
    }

    public Set<me.belakede.thesis.server.game.domain.Player> transform(Collection<Player> players) {
        return players.stream().map(this::transform).collect(Collectors.toSet());
    }

    public Player transform(me.belakede.thesis.server.game.domain.Player player) {
        return new DefaultPlayer(player.getSuspect(), cardTransformer.transform(player.getPlayerCards()));
    }

    public me.belakede.thesis.server.game.domain.Player transform(Player player) {
        me.belakede.thesis.server.game.domain.Player result = new me.belakede.thesis.server.game.domain.Player();
        result.setPlayerCards(cardTransformer.transform(player.getCards()));
        result.setSuspect(player.getFigurine());
        return result;
    }

}
