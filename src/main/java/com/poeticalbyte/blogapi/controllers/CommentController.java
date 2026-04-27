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

import com.poeticalbyte.blogapi.entities.Comment;
import com.poeticalbyte.blogapi.entities.Post;
import com.poeticalbyte.blogapi.entities.User;
import com.poeticalbyte.blogapi.repositories.CommentRepository;
import com.poeticalbyte.blogapi.repositories.PostRepository;
import com.poeticalbyte.blogapi.repositories.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/posts/{postId}/comments")
@CrossOrigin(origins = "*")
public class CommentController {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentController(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // --- R E A D ---
    // Read All (by Post):
    @GetMapping
    public List<Comment> getAllCommentsByPost(@PathVariable Long postId) {
        return commentRepository.findByPostId(postId);
    }
    // Read By ID:
    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable Long postId, @PathVariable Long id) {
        return commentRepository.findById(Objects.requireNonNull(id)).orElse(null);
    }

    // --- C R E A T E ---
    @PostMapping
    public ResponseEntity<Comment> createComment(@PathVariable Long postId, @RequestBody Comment comment) {
        Post post = postRepository.findById(Objects.requireNonNull(postId)).orElse(null);
        User user = comment.getUser() != null && comment.getUser().getId() != null
            ? userRepository.findById(comment.getUser().getId()).orElse(null)
            : null;

        if (post != null && user != null) {
            comment.setPost(post);
            comment.setUser(user);
            return ResponseEntity.ok(commentRepository.save(comment));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // --- U P D A T E ---
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long postId, @PathVariable Long id, @RequestBody Comment comment) {
        return commentRepository.findById(Objects.requireNonNull(id))
        .map(existing -> {
            existing.setContent(comment.getContent());

            return ResponseEntity.ok(commentRepository.save(existing));
        })
        .orElse(ResponseEntity.notFound().build());
    }


    // --- D E L E T E ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Comment> deleteComment(@PathVariable Long postId, @PathVariable Long id) {
        Comment comment = commentRepository.findById(Objects.requireNonNull(id)).orElse(null);
        
        if (comment != null) {
            commentRepository.delete(comment);
            return ResponseEntity.ok(comment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
