package me.belakede.thesis.server.game.repository;


import me.belakede.thesis.server.game.domain.Game;
import me.belakede.thesis.server.game.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findByGameOrderByOrdinalNumberAsc(Game game);

}
