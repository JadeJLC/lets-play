package com.project.lets_play.service;

import org.springframework.stereotype.Service;

import com.project.lets_play.repository.ProductRepository;
import com.project.lets_play.model.Product;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(String id) {
        productRepository.delete(readProduct(id));
        return;
    }

    public Product readProduct(String id) {
        return productRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public Product updateProduct(Product product) {
       return  createProduct(product);
    }
    
}
