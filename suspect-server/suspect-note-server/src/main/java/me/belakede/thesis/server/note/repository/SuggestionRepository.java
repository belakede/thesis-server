package me.belakede.thesis.server.note.repository;

import me.belakede.thesis.server.note.domain.Author;
import me.belakede.thesis.server.note.domain.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {

    Suggestion findByAuthor(Author author);

}
