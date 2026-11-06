package org.example.practice5.service;

import org.example.practice5.model.Product;

public interface ProductService {
    Product getProductById(Long id);
    Product updateProduct(Long id, String name, double price);
}