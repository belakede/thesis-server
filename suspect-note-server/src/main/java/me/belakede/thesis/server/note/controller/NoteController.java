package me.belakede.thesis.server.note.controller;

import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.internal.game.util.Cards;
import me.belakede.thesis.server.note.domain.Author;
import me.belakede.thesis.server.note.domain.Note;
import me.belakede.thesis.server.note.exception.MissingAuthorException;
import me.belakede.thesis.server.note.request.NoteRequest;
import me.belakede.thesis.server.note.response.NoteResponse;
import me.belakede.thesis.server.note.response.NotesResponse;
import me.belakede.thesis.server.note.service.AuthorService;
import me.belakede.thesis.server.note.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    public void join(Principal principal, @RequestBody NoteRequest noteRequest) {
        authorService.create(principal.getName(), noteRequest.getRoom());
    }

    @RequestMapping(value = "/{room}", method = RequestMethod.GET)
    public NotesResponse findAll(Principal principal, @PathVariable("room") String room) throws MissingAuthorException {
        Author author = authorService.findByNameAndRoom(principal.getName(), room);
        List<Note> notes = noteService.findAllByAuthor(author);
        Set<NoteResponse> noteResponses = notes.stream().map(n -> new NoteResponse(n.getCard(), n.getOwner(), n.getMarker())).collect(Collectors.toSet());
        return new NotesResponse(noteResponses);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void store(Principal principal, @RequestBody NoteRequest noteRequest) throws MissingAuthorException {
        Author author = authorService.findByNameAndRoom(principal.getName(), noteRequest.getRoom());
        Optional<Card> card = Cards.valueOf(noteRequest.getCard());
        noteService.store(new Note(author, noteRequest.getOwner(), card.orElse(null), noteRequest.getMarker()));
    }

}
