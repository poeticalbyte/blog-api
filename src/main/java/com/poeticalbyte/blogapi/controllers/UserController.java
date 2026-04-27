package com.poeticalbyte.blogapi.controllers;

import java.util.List;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poeticalbyte.blogapi.entities.User;
import com.poeticalbyte.blogapi.repositories.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // --- R E A D ---
    // Read All:
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    // Read By ID:
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(Objects.requireNonNull(id)).orElse(null);
    }

    // --- C R E A T E ---
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(Objects.requireNonNull(user));
    }

    // --- U P D A T E ---
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userRepository.findById(Objects.requireNonNull(id))
        .map(existing -> {
            existing.setUsername(user.getUsername());
            existing.setEmail(user.getEmail());

            return ResponseEntity.ok(userRepository.save(existing));
        })
        .orElse(ResponseEntity.notFound().build());
    }


    // --- D E L E T E ---
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(Objects.requireNonNull(id)).orElse(null);
        
        if (user != null) {
            userRepository.delete(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
