package me.belakede.thesis.server.note.controller;

import me.belakede.thesis.game.equipment.Suspicion;
import me.belakede.thesis.server.note.domain.Author;
import me.belakede.thesis.server.note.domain.Suggestion;
import me.belakede.thesis.server.note.exception.MissingAuthorException;
import me.belakede.thesis.server.note.exception.MissingSuggestionException;
import me.belakede.thesis.server.note.service.AuthorService;
import me.belakede.thesis.server.note.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.security.Principal;

@RestController
@RequestMapping("/suggestions")
public class SuggestionController {

    private final AuthorService authorService;
    private final SuggestionService suggestionService;

    @Autowired
    public SuggestionController(AuthorService authorService, SuggestionService suggestionService) {
        this.authorService = authorService;
        this.suggestionService = suggestionService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Suggestion read(Principal principal, @RequestParam @NotNull String room) throws MissingAuthorException, MissingSuggestionException {
        Author author = authorService.findByNameAndRoom(principal.getName(), room);
        return suggestionService.read(author);
    }

    @RequestMapping(value = "/{room}", method = RequestMethod.POST)
    public Suggestion store(Principal principal, @PathVariable("room") String room, Suspicion suspicion) throws MissingAuthorException {
        System.err.println("suspicion: " + suspicion);
        Author author = authorService.findByNameAndRoom(principal.getName(), room);
        return suggestionService.store(author, suspicion);
    }
}
