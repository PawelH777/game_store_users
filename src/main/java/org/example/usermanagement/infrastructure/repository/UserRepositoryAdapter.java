package org.example.usermanagement.infrastructure.repository;

import org.example.usermanagement.domain.model.UserDO;

import java.util.Set;

public interface UserRepositoryAdapter {

    Set<UserDO> findUsersWithUsername(String username);

    long save(UserDO userDraft);
}
