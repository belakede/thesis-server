package me.belakede.thesis.server.game.repository;

import me.belakede.thesis.server.game.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findByStatus(Game.Status status);

}
