package com.project.lets_play.service;

import org.springframework.stereotype.Service;

import com.project.lets_play.repository.UserRepository;
import com.project.lets_play.model.User;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.delete(readUser(id));
        return;
    }

    public User readUser(String id) {
        return userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public User updateUser(User user) {
      
       return  createUser(user);
    }
    
}
