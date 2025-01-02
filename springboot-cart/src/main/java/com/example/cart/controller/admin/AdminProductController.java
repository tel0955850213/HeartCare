package com.example.cart.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.cart.model.entity.Product;
import com.example.cart.response.ApiResponse;
import com.example.cart.service.ProductService;
import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ApiResponse<List<Product>> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            return ApiResponse.success("查詢成功", products);
        } catch (Exception e) {
            return ApiResponse.error("查詢失敗: " + e.getMessage());
        }
    }

    @PostMapping
    public ApiResponse<Product> createProduct(@RequestBody Product product) {
        try {
            Product savedProduct = productService.saveProduct(product);
            return ApiResponse.success("創建成功", savedProduct);
        } catch (Exception e) {
            return ApiResponse.error("創建失敗: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            product.setId(id);
            Product updatedProduct = productService.updateProduct(product);
            return ApiResponse.success("更新成功", updatedProduct);
        } catch (Exception e) {
            return ApiResponse.error("更新失敗: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ApiResponse.success("刪除成功", null);
        } catch (Exception e) {
            return ApiResponse.error("刪除失敗: " + e.getMessage());
        }
    }
} 