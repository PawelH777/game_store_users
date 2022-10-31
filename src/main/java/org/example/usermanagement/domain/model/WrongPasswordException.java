package org.example.usermanagement.domain.model;

import lombok.Getter;

@Getter
public class WrongPasswordException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Wrong password!";

    public WrongPasswordException() {
        super(ERROR_MESSAGE);
    }
}
