package com.example.cart.service;

import com.example.cart.model.dto.UserDTO;

public interface AuthService {
    UserDTO login(String username, String password);
    UserDTO register(String username, String password, String email, String role);
} 