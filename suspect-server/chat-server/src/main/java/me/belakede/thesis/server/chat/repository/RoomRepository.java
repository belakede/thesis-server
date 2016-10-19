package me.belakede.thesis.server.chat.repository;

import me.belakede.thesis.server.chat.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findAll();

    Room findByName(String name);

}
