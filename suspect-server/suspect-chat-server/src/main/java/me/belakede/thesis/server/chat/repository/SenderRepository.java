package me.belakede.thesis.server.chat.repository;

import me.belakede.thesis.server.chat.domain.Sender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SenderRepository extends JpaRepository<Sender, Long> {

    Sender findByNameAndRoom(String name, String room);

}
