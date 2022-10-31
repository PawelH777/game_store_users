package org.example.usermanagement.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDO {

    private long id;

    private String username;

    private String hashedPassword;
}
