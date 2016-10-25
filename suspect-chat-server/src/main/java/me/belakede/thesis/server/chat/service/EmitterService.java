package me.belakede.thesis.server.chat.service;

import me.belakede.thesis.server.chat.domain.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmitterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmitterService.class);
    private final List<SseEmitter> emitters;

    public EmitterService() {
        this.emitters = new ArrayList<>();
    }

    public SseEmitter createEmitter() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);
        return emitter;
    }

    public void broadcast(Message message) {
        emitters.forEach((SseEmitter emitter) -> {
            try {
                emitter.send(message, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(emitter);
                LOGGER.warn("Can't broadcast the message for emitter {} - Connection closed.", emitter);
                LOGGER.warn("{}", e);
            }
        });
    }

    public void close() {
        emitters.forEach(ResponseBodyEmitter::complete);
        emitters.clear();
    }

}
