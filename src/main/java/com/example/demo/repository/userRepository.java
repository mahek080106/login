package com.example.demo.repository;

import com.example.demo.model.user;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface userRepository extends MongoRepository<user, String> {

    Optional<user> findByEmail(String email);

    boolean existsByEmail(String email);
}