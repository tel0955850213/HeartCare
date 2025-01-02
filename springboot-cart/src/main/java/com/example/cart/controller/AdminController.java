package com.example.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cart.service.OrderService;
import com.example.cart.service.UserService;
import com.example.cart.service.ProductService;
import com.example.cart.model.dto.OrderDTO;
import com.example.cart.model.dto.UserDTO;
import com.example.cart.model.entity.OrderStatus;
import com.example.cart.response.ApiResponse;
import com.example.cart.exception.ProductNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminController {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProductService productService;
    
    // 用戶管理
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        try {
            return ResponseEntity.ok(
                ApiResponse.success("獲取用戶列表成功", userService.getAllUsers())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("獲取用戶列表失敗: " + e.getMessage()));
        }
    }
    
    // 儀表板統計
    @GetMapping("/dashboard/stats")
    public ApiResponse<Map<String, Object>> getDashboardStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalOrders", orderService.getTotalOrders());
            stats.put("monthlyRevenue", orderService.getMonthlyRevenue());
            stats.put("totalProducts", productService.getTotalProducts());
            return ApiResponse.success("查詢成功", stats);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
} 