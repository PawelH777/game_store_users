package org.example.usermanagement.domain.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.usermanagement.domain.model.UserAlreadyInDatabaseException;
import org.example.usermanagement.domain.model.UserDO;
import org.example.usermanagement.domain.model.UserNotFoundException;
import org.example.usermanagement.domain.model.WrongPasswordException;
import org.example.usermanagement.infrastructure.repository.UserRepositoryAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    public static String USER_ID_KEY = "user_id";
    public static String TOKEN_KEY = "token";

    private final PasswordEncoder passwordEncoder;

    private final UserRepositoryAdapter userRepositoryAdapter;

    public UserServiceImpl(final PasswordEncoder passwordEncoder, final UserRepositoryAdapter userRepositoryAdapter) {
        this.passwordEncoder = passwordEncoder;
        this.userRepositoryAdapter = userRepositoryAdapter;
    }

    @Override
    public long registerUser(final String username, final String password) {
        final Set<UserDO> usersWithUsername = userRepositoryAdapter.findUsersWithUsername(username);
        if (usersWithUsername.size() > 0) {
            throw new UserAlreadyInDatabaseException(username);
        }

        final UserDO user = UserDO.builder()
                .username(username)
                .hashedPassword(passwordEncoder.encode(username))
                .build();

        return userRepositoryAdapter.save(user);
    }

    @Override
    public Map<String, ?> loginUser(final String username, final String password) {
        final Set<UserDO> usersWithUsername = userRepositoryAdapter.findUsersWithUsername(username);
        if (usersWithUsername.size() == 0) {
            throw new UserNotFoundException(username);
        }

        final UserDO user = usersWithUsername.iterator().next();
        if(!passwordEncoder.matches(password, user.getHashedPassword())) {
            throw new WrongPasswordException();
        }

        final String token = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "secret")
                .compact();

        return Map.of(TOKEN_KEY, token, USER_ID_KEY, user.getId());
    }
}
