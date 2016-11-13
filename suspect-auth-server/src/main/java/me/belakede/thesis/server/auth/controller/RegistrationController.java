package me.belakede.thesis.server.auth.controller;

import me.belakede.thesis.server.auth.domain.User;
import me.belakede.thesis.server.auth.request.UserRequest;
import me.belakede.thesis.server.auth.response.UserResponse;
import me.belakede.thesis.server.auth.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserResponse register(@RequestBody UserRequest userRequest) {
        User user = userService.create(userRequest.getUsername(), userRequest.getPassword());
        return new UserResponse(user.getUsername());
    }
}
