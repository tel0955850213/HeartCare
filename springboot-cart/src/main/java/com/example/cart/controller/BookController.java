package com.example.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.cart.model.entity.Product;
import com.example.cart.response.ApiResponse;
import com.example.cart.service.BookService;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class BookController {
    
    @Autowired
    private BookService bookService;
    
    @GetMapping("/recommend")
    public ApiResponse<Product> getRecommendedBook(@RequestParam String type) {
        try {
            Product book = bookService.getRecommendationByType(type);
            return ApiResponse.success("獲取推薦成功", book);
        } catch (Exception e) {
            return ApiResponse.error("獲取推薦失敗: " + e.getMessage());
        }
    }
} 