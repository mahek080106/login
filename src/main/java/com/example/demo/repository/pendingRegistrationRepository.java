package com.example.demo.repository;

import com.example.demo.model.PendingRegistration;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface pendingRegistrationRepository
        extends MongoRepository<PendingRegistration, String> {

    Optional<PendingRegistration> findByEmail(String email);

    void deleteByEmail(String email);
}