package me.belakede.thesis.server.note.service;

import me.belakede.thesis.server.note.domain.Author;
import me.belakede.thesis.server.note.exception.MissingAuthorException;
import me.belakede.thesis.server.note.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author create(String name, String room) {
        Author author = authorRepository.findByNameAndRoom(name, room);
        if (author == null) {
            author = authorRepository.save(new Author(name, room));
        }
        return author;
    }

    public Author findByNameAndRoom(String name, String room) throws MissingAuthorException {
        Author author = authorRepository.findByNameAndRoom(name, room);
        if (author == null) {
            throw new MissingAuthorException("The following author is not found: " + name);
        }
        return author;
    }

    public void deleteByRoom(String room) {
        authorRepository.deleteByRoom(room);
    }

}
