package com.project.lets_play.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.project.lets_play.model.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    
}
