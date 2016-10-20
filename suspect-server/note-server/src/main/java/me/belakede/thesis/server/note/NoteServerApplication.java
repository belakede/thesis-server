package me.belakede.thesis.server.note;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("note-server.properties")
public class NoteServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoteServerApplication.class, args);
    }

}
