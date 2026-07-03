package com.project.lets_play.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Document(collection = "products") // Tells MongoDB this is a database collection
@Data

public class Product {

    @Id
    private String id;
    private String name;
    private String description;
    private Double price;
    private String userId;
}
