package me.belakede.thesis.server.note.repository;

import me.belakede.thesis.server.note.domain.Author;
import me.belakede.thesis.server.note.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByAuthor(Author author);

}
