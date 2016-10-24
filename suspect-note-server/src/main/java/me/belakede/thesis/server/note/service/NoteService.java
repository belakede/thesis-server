package me.belakede.thesis.server.note.service;

import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Marker;
import me.belakede.thesis.server.note.domain.Author;
import me.belakede.thesis.server.note.domain.Note;
import me.belakede.thesis.server.note.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note create(Author author, Card card, String owner, Marker marker) {
        return noteRepository.save(new Note(author, card.name(), owner, marker));
    }

    public List<Note> findAllByAuthor(Author author) {
        return noteRepository.findByAuthor(author);
    }

}
