package com.example.cart.model.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String role;  // 使用字符串，前端傳入 "USER" 或 "ADMIN"
} 