package org.example.usermanagement.application.controller;


import org.example.usermanagement.application.model.CredentialsDTO;
import org.example.usermanagement.application.model.LoginResponse;
import org.example.usermanagement.domain.model.UserAlreadyInDatabaseException;
import org.example.usermanagement.domain.model.UserNotFoundException;
import org.example.usermanagement.domain.model.WrongPasswordException;
import org.example.usermanagement.domain.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    ResponseEntity<Long> registerUser(@RequestBody final CredentialsDTO credentials) {
        if (credentials.getUsername() == null || credentials.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Username and password needs to be provided");
        }

        final long userId;
        try {
            userId = userService.registerUser(credentials.getUsername(), credentials.getPassword());
        } catch (final UserAlreadyInDatabaseException ex) {
            logger.error("User with username {} is already in database, error stacktrace: ", ex.getUsername(), ex);
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "User with username " + ex.getUsername() + " is already in database");
        }

        return ResponseEntity.status(200)
                .body(userId);
    }

    @PostMapping("/login")
    ResponseEntity<LoginResponse> loginUser(@RequestBody final CredentialsDTO credentials) {
        if (credentials.getUsername() == null || credentials.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Username and password needs to be provided");
        }

        final Map<String, ?> loginOutput;
        try {
            loginOutput = userService.loginUser(credentials.getUsername(), credentials.getPassword());
        } catch (final UserNotFoundException ex) {
            logger.error("User with username {} not found, error stacktrace: ", ex.getUsername(), ex);
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "User with username " + ex.getUsername() + " not found in database");
        } catch (final WrongPasswordException ex) {
            logger.error("Wrong password, error stacktrace: ", ex);
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Wrong password");
        }

        return ResponseEntity.status(200)
                .body(buildLoginResponse(loginOutput));
    }

    private LoginResponse buildLoginResponse(final Map<String, ?> loginOutput) {
        if (!(loginOutput.get("user_id") instanceof Long userId) || !(loginOutput.get("token") instanceof String token)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User id or token was returned in wrong type");
        }

        return LoginResponse.builder()
                .userId(userId)
                .token(token)
                .build();
    }


}
