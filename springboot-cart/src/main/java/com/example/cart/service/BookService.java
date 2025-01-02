package com.example.cart.service;

import com.example.cart.model.entity.Product;

public interface BookService {
    Product getRecommendationByType(String type);
} 