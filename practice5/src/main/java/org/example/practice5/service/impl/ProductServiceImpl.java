package org.example.practice5.service.impl;

import org.example.practice5.model.Product;
import org.example.practice5.service.ProductService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Override
    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return new Product(id, "Laptop", 1200.00);
    }

    @Override
    @CacheEvict(value = "products", key = "#id")
    public Product updateProduct(Long id, String name, double price) {
        return new Product(id, name, price);
    }
}