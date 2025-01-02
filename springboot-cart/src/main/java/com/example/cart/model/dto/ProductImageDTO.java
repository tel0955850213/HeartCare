package com.example.cart.model.dto;

import lombok.Data;

@Data
public class ProductImageDTO {
    private Long id;
    private String imageBase64;
    private Long productId;  // 關聯的產品ID
} 