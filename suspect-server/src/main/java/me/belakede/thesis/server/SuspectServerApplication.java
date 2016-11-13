package me.belakede.thesis.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
        me.belakede.thesis.server.auth.AuthServerApplication.class,
        me.belakede.thesis.server.chat.ChatServerApplication.class,
        me.belakede.thesis.server.note.NoteServerApplication.class,
        me.belakede.thesis.server.game.GameServerApplication.class
})
public class SuspectServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuspectServerApplication.class, args);
    }

}
