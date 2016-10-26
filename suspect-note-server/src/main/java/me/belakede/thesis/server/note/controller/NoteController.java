package me.belakede.thesis.server.note.controller;

import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Marker;
import me.belakede.thesis.server.note.domain.Author;
import me.belakede.thesis.server.note.domain.Note;
import me.belakede.thesis.server.note.exception.MissingAuthorException;
import me.belakede.thesis.server.note.service.AuthorService;
import me.belakede.thesis.server.note.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/notes")
@EnableResourceServer
public class NoteController {

    private final NoteService noteService;
    private final AuthorService authorService;

    @Autowired
    public NoteController(NoteService noteService, AuthorService authorService) {
        this.noteService = noteService;
        this.authorService = authorService;
    }

    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public Author join(Principal principal, @NotNull String room) {
        return authorService.create(principal.getName(), room);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Note> findAll(Principal principal, @RequestParam @NotNull String room) throws MissingAuthorException {
        Author author = authorService.findByNameAndRoom(principal.getName(), room);
        return noteService.findAllByAuthor(author);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Note store(Principal principal, @RequestParam @NotNull String room, @RequestParam @NotNull Card card,
                      @RequestParam @NotNull String owner, @RequestParam @NotNull Marker marker) throws MissingAuthorException {
        Author author = authorService.findByNameAndRoom(principal.getName(), room);
        return noteService.create(new Note(author, owner, card, marker));
    }

}
