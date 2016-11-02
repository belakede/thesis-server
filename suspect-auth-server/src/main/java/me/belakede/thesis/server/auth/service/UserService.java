package me.belakede.thesis.server.auth.service;

import me.belakede.thesis.server.auth.domain.Role;
import me.belakede.thesis.server.auth.domain.User;
import me.belakede.thesis.server.auth.exception.MissingUserException;
import me.belakede.thesis.server.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    /**
     * @param username
     * @return
     * @throws MissingUserException
     */
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new MissingUserException("User not found: " + username);
        }
        return user;
    }

    public List<User> findByRole(Role role) {
        return userRepository.findByRolesContains(role);
    }

    public User create(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setEnabled(true);
        user.setRoles(Stream.of(Role.USER).collect(Collectors.toSet()));
        return userRepository.save(user);
    }

    /**
     * @param username
     * @return
     * @throws MissingUserException
     */
    public User remove(String username) {
        User user = findByUsername(username);
        userRepository.delete(user);
        return user;
    }
}
