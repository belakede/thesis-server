package me.belakede.thesis.server.note.service;

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

    public Note create(Note note) {
        return noteRepository.save(note);
    }

    public List<Note> findAllByAuthor(Author author) {
        return noteRepository.findByAuthor(author);
    }

}
