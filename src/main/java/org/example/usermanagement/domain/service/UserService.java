package org.example.usermanagement.domain.service;

import java.util.Map;

public interface UserService {
    long registerUser(String username, String password);

    Map<String, ?> loginUser(String username, String password);
}
