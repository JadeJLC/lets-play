package com.project.lets_play.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Document(collection = "users") // Tells MongoDB this is a database collection
@Data
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private String role;
}
