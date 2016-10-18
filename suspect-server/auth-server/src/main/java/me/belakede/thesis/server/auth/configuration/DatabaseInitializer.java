package me.belakede.thesis.server.auth.configuration;

import me.belakede.thesis.server.auth.domain.Role;
import me.belakede.thesis.server.auth.domain.User;
import me.belakede.thesis.server.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class DatabaseInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Value("${suspect.server.admin.username}")
    private String username;

    @Value("${suspect.server.admin.password}")
    private String password;

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public DatabaseInitializer(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @PostConstruct
    public void initIt() throws Exception {
        User admin = userRepository.findByUsername(username);
        if (null == admin) {
            LOGGER.info("Admin user is missing");
            userRepository.save(createAdminUser());
            LOGGER.info("{} user created with password {}", username, password);
        }
    }

    private User createAdminUser() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setRoles(Stream.of(Role.values()).collect(Collectors.toSet()));
        user.setEnabled(true);
        return user;
    }

}
