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

import com.poeticalbyte.blogapi.entities.Post;
import com.poeticalbyte.blogapi.entities.User;
import com.poeticalbyte.blogapi.repositories.PostRepository;
import com.poeticalbyte.blogapi.repositories.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/users/{userId}/posts")
@CrossOrigin(origins = "*")
public class PostController {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostController(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // --- R E A D ---
    // Read All (by User):
    @GetMapping
    public List<Post> getAllPostsByUser(@PathVariable Long userId) {
        return postRepository.findByUserId(userId);
    }
    // Read By ID:
    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long userId, @PathVariable Long id) {
        return postRepository.findById(Objects.requireNonNull(id)).orElse(null);
    }

    // --- C R E A T E ---
    @PostMapping
    public ResponseEntity<Post> createPost(@PathVariable Long userId, @RequestBody Post post) {
        User user = userRepository.findById(Objects.requireNonNull(userId)).orElse(null);

        if (user != null) {
            post.setUser(user);
            return ResponseEntity.ok(postRepository.save(post));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // --- U P D A T E ---
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long userId, @PathVariable Long id, @RequestBody Post post) {
        return postRepository.findById(Objects.requireNonNull(id))
        .map(existing -> {
            existing.setContent(post.getContent());
            existing.setHeadline(post.getHeadline());

            return ResponseEntity.ok(postRepository.save(existing));
        })
        .orElse(ResponseEntity.notFound().build());
    }


    // --- D E L E T E ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Post> deletePost(@PathVariable Long userId, @PathVariable Long id) {
        Post post = postRepository.findById(Objects.requireNonNull(id)).orElse(null);
        
        if (post != null) {
            postRepository.delete(post);
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
