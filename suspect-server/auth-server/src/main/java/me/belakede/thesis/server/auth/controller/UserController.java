package me.belakede.thesis.server.auth.controller;

import me.belakede.thesis.server.auth.domain.Role;
import me.belakede.thesis.server.auth.domain.User;
import me.belakede.thesis.server.auth.exception.MissingUserException;
import me.belakede.thesis.server.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@EnableResourceServer
public class UserController {

    @Autowired
    private UserService service;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> getUsers() {
        return service.findByRole(Role.USER);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public User createUser(@RequestParam String username, @RequestParam String password) {
        return service.create(username, password);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.DELETE)
    public User removeUser(@RequestParam String username) throws MissingUserException {
        return service.remove(username);
    }

}
