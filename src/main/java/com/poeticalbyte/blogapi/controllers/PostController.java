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
import com.poeticalbyte.blogapi.repositories.PostRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostController {
    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // --- R E A D ---
    // Read All:
    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    // Read By ID:
    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postRepository.findById(Objects.requireNonNull(id)).orElse(null);
    }

    // --- C R E A T E ---
    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postRepository.save(Objects.requireNonNull(post));
    }

    // --- U P D A T E ---
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id,@RequestBody Post post) {
        return postRepository.findById(Objects.requireNonNull(id))
        .map(existing -> {
            existing.setAuthor(post.getAuthor());
            existing.setContent(post.getContent());
            existing.setHeadline(post.getHeadline());

            return ResponseEntity.ok(postRepository.save(existing));
        })
        .orElse(ResponseEntity.notFound().build());
    }


    // --- D E L E T E ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Post> deletePost(@PathVariable Long id) {
        Post post = postRepository.findById(Objects.requireNonNull(id)).orElse(null);
        
        if (post != null) {
            postRepository.delete(post);
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
