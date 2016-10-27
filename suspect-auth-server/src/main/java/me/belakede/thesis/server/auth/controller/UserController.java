package me.belakede.thesis.server.auth.controller;

import me.belakede.thesis.server.auth.domain.Role;
import me.belakede.thesis.server.auth.domain.User;
import me.belakede.thesis.server.auth.exception.MissingUserException;
import me.belakede.thesis.server.auth.request.UserRequest;
import me.belakede.thesis.server.auth.response.UserResponse;
import me.belakede.thesis.server.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(value = "/users", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserResponse createUser(@RequestBody UserRequest userRequest) {
        User user = service.create(userRequest.getUsername(), userRequest.getPassword());
        return new UserResponse(user.getUsername());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/users/{username}", method = RequestMethod.DELETE)
    public UserResponse removeUser(@PathVariable("username") String username) throws MissingUserException {
        User user = service.remove(username);
        return new UserResponse(user.getUsername());
    }

}
