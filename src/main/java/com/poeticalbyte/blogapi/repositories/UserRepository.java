package com.poeticalbyte.blogapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poeticalbyte.blogapi.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
