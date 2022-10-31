package org.example.usermanagement.infrastructure.repository;

import org.example.usermanagement.domain.model.UserDO;
import org.example.usermanagement.infrastructure.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryAdapterImpl implements UserRepositoryAdapter {

    private final UserRepository userRepository;

    public UserRepositoryAdapterImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Set<UserDO> findUsersWithUsername(final String username) {
        final List<User> users = userRepository.findByUsername(username);

        return users.stream()
                .map(this::buildUserDomainObject)
                .collect(Collectors.toSet());
    }

    @Override
    public long save(final UserDO userDraft) {
        final User user = userRepository.save(buildUserEntity(userDraft));
        return user.getId();
    }

    private UserDO buildUserDomainObject(final User user) {
        return UserDO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .hashedPassword(user.getHashedPassword())
                .build();
    }

    private User buildUserEntity(final UserDO userDO) {
        return User.builder()
                .id(userDO.getId())
                .username(userDO.getUsername())
                .hashedPassword(userDO.getHashedPassword())
                .build();
    }
}
