package org.example.usermanagement.domain.model;

import lombok.Getter;

@Getter
public class UserAlreadyInDatabaseException extends RuntimeException {
    private static final String ERROR_MESSAGE = "User already exist in database!";

    private final String username;

    public UserAlreadyInDatabaseException(final String username) {
        super(ERROR_MESSAGE);
        this.username = username;
    }
}
