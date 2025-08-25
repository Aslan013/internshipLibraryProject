package com.example.trial.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.trial.entity.User;
import com.example.trial.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository repo;

    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody User user) {

        // Check for empty fields
        if(user.getUsername() == null || user.getUsername().isEmpty() ||
           user.getPassword() == null || user.getPassword().isEmpty() ||
           user.getFirstName() == null || user.getFirstName().isEmpty() ||
           user.getLastName() == null || user.getLastName().isEmpty() ||
           user.getTc() == null || user.getTc().isEmpty() ||
           user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Alanlar boş olamaz");
        }

        // Check if username already exists
        if(repo.existsById(user.getUsername())){
            return ResponseEntity.badRequest().body("Kullanıcı zaten var");
        }

        // Set default isAdmin to false
        if(user.getIsAdmin() == null) {
            user.setIsAdmin(false);
        }

        // Save user to database
        repo.save(user);

        return ResponseEntity.ok("Kullanıcı eklendi");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User login) {
        return repo.findById(login.getUsername())
            .map(user -> {
                if(user.getPassword().equals(login.getPassword())){
                    return ResponseEntity.ok("Giriş başarılı");
                } else {
                    return ResponseEntity.badRequest().body("Şifre yanlış");
                }
            })
            .orElse(ResponseEntity.badRequest().body("Kullanıcı bulunamadı"));
    }
}



