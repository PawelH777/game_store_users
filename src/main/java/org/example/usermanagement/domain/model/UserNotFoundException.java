package org.example.usermanagement.domain.model;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE = "User not found in database!";

    private final String username;

    public UserNotFoundException(final String username) {
        super(ERROR_MESSAGE);
        this.username = username;
    }
}
