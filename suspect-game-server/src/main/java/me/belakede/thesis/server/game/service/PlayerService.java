package me.belakede.thesis.server.game.service;

import me.belakede.thesis.server.game.converter.PlayerConverter;
import me.belakede.thesis.server.game.repository.PlayerRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerConverter converter;

    public PlayerService(PlayerRepository playerRepository, PlayerConverter converter) {
        this.playerRepository = playerRepository;
        this.converter = converter;
    }


}
