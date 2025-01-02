package com.example.cart.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.cart.model.entity.Product;
import com.example.cart.repository.ProductRepository;
import com.example.cart.service.ProductService;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    
    @Override
    public Product updateProduct(Product product) {
        Product existingProduct = productRepository.findById(product.getId())
            .orElseThrow(() -> new RuntimeException("商品不存在"));
            
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setDescription(product.getDescription());
        if (product.getImageUrl() != null) {
            existingProduct.setImageUrl(product.getImageUrl());
        }
        
        return productRepository.save(existingProduct);
    }
    
    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("商品不存在: " + id));
    }
    
    @Override
    public long getTotalProducts() {
        return productRepository.count();
    }
}
