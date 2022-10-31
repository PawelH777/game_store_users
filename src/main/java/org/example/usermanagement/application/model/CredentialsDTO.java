package org.example.usermanagement.application.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CredentialsDTO {

    private String username;

    private String password;
}
