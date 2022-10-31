package org.example.usermanagement.infrastructure.repository;

import org.example.usermanagement.infrastructure.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByUsername(String username);
}
