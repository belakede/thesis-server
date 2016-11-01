package me.belakede.thesis.server.game.repository;

import me.belakede.thesis.server.game.domain.Game;
import me.belakede.thesis.server.game.domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    List<Position> findByGame(Game game);

}
