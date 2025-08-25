package com.example.trial.repository;


import com.example.trial.entity.User; // <-- this is needed
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> { 
    boolean existsByUsername(String username);
    User findByUsername(String username);
}
