package com.project.lets_play.model;

import org.springframework.data.annotation.Id;
import lombok.Data;

@Data
public class UserResponse {
    @Id
    private String id;
    private String name;
    private String email;
    private String role;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
    
}
