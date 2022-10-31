package org.example.usermanagement.infrastructure.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "ACCOUNT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column
    private String username;

    @Column
    private String hashedPassword;
}
