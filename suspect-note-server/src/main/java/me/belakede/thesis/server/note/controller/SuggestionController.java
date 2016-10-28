package me.belakede.thesis.server.note.controller;

import me.belakede.thesis.internal.game.equipment.DefaultSuspicion;
import me.belakede.thesis.server.note.domain.Author;
import me.belakede.thesis.server.note.domain.Suggestion;
import me.belakede.thesis.server.note.exception.MissingAuthorException;
import me.belakede.thesis.server.note.exception.MissingSuggestionException;
import me.belakede.thesis.server.note.request.SuggestionRequest;
import me.belakede.thesis.server.note.response.SuggestionResponse;
import me.belakede.thesis.server.note.service.AuthorService;
import me.belakede.thesis.server.note.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/{room}", method = RequestMethod.GET)
    public SuggestionResponse read(Principal principal, @PathVariable("room") String room) throws MissingAuthorException, MissingSuggestionException {
        Author author = authorService.findByNameAndRoom(principal.getName(), room);
        Suggestion suggestion = suggestionService.read(author);
        return new SuggestionResponse(new DefaultSuspicion(suggestion.getSuspect(), suggestion.getRoom(), suggestion.getWeapon()));
    }

    @RequestMapping(value = "/{room}", method = RequestMethod.POST)
    public void store(Principal principal, @PathVariable("room") String room, @RequestBody SuggestionRequest suggestionRequest) throws MissingAuthorException {
        Author author = authorService.findByNameAndRoom(principal.getName(), room);
        suggestionService.store(author, new DefaultSuspicion(suggestionRequest.getSuspect(), suggestionRequest.getRoom(), suggestionRequest.getWeapon()));
    }
}
