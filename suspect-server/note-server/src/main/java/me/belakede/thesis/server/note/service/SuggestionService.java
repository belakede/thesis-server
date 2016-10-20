package me.belakede.thesis.server.note.service;

import me.belakede.thesis.game.equipment.Suspicion;
import me.belakede.thesis.server.note.domain.Author;
import me.belakede.thesis.server.note.domain.Suggestion;
import me.belakede.thesis.server.note.repository.SuggestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuggestionService {

    private final SuggestionRepository suggestionRepository;

    @Autowired
    public SuggestionService(SuggestionRepository suggestionRepository) {
        this.suggestionRepository = suggestionRepository;
    }

    public Suggestion store(Author author, Suspicion suspicion) {
        Suggestion suggestion = suggestionRepository.findByAuthor(author);
        if (suggestion == null) {
            suggestion = new Suggestion(author, suspicion.getSuspect(), suspicion.getRoom(), suspicion.getWeapon());
        } else {
            suggestion.setSuspect(suspicion.getSuspect());
            suggestion.setRoom(suspicion.getRoom());
            suggestion.setWeapon(suspicion.getWeapon());
        }
        return suggestionRepository.save(suggestion);
    }

}
