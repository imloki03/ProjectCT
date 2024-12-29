package com.projectct.authservice.repository;

import com.projectct.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    User findByUsernameOrEmail(String username, String email);
}