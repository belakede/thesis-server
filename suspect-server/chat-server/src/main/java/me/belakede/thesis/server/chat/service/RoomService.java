package me.belakede.thesis.server.chat.service;

import me.belakede.thesis.server.chat.domain.Room;
import me.belakede.thesis.server.chat.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoomService {

    private final RoomRepository repository;

    @Autowired
    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    public Room create() {
        return repository.save(new Room(UUID.randomUUID().toString()));
    }


}
