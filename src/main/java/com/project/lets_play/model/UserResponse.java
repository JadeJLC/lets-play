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

    public UserResponse(String id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }
    
}
