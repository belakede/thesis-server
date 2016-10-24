package me.belakede.thesis.server.note.repository;

import me.belakede.thesis.server.note.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Author findByNameAndRoom(String name, String room);

}
