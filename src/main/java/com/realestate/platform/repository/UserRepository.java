package com.realestate.platform.repository;

import com.realestate.platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Spring Data JPA automatically creates this query for us
    // It's used by Spring Security to find a user by their email (username)
    Optional<User> findByEmail(String email);
    
    // We'll use this to check if an email is already registered
    boolean existsByEmail(String email);
}