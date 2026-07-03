package com.project.lets_play.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.project.lets_play.model.User;

public interface UserRepository extends MongoRepository<User, String> {
    
}
