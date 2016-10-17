package me.belakede.thesis.server.auth.controller;

import me.belakede.thesis.server.auth.domain.User;
import me.belakede.thesis.server.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@EnableResourceServer
public class UserController {

    @Autowired
    private UserService service;

    @RequestMapping("/users/me")
    public Principal getCurrentLoggedInUser(Principal user) {
        return user;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> list() {
        return service.findAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public User create(@RequestParam String username, @RequestParam String password) {
        return service.create(username, password);
    }

}
