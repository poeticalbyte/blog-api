package com.poeticalbyte.blogapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poeticalbyte.blogapi.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    
}
