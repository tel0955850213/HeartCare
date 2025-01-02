package com.example.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.cart.model.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
