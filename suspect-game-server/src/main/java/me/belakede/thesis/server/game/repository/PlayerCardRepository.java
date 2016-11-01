package me.belakede.thesis.server.game.repository;

import me.belakede.thesis.server.game.domain.Player;
import me.belakede.thesis.server.game.domain.PlayerCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerCardRepository extends JpaRepository<PlayerCard, Long> {

    List<PlayerCard> findByPlayer(Player player);

}
