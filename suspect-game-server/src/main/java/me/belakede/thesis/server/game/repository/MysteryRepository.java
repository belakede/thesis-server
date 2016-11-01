package me.belakede.thesis.server.game.repository;

import me.belakede.thesis.server.game.domain.Game;
import me.belakede.thesis.server.game.domain.Mystery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MysteryRepository extends JpaRepository<Mystery, Long> {

    Mystery findByGame(Game game);


}
