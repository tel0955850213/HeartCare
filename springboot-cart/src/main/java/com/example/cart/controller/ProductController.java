package com.example.cart.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.cart.aop.CheckUserSession;
import com.example.cart.model.dto.ProductDTO;
import com.example.cart.response.ApiResponse;
import com.example.cart.service.ProductService;
import com.example.cart.model.entity.Product;

/*
 * WEB REST API
 * ----------------------------------
 * Servlet-Path: /api/products
 * ----------------------------------
 * GET   /      查詢所有商品(多筆)
 * GET   /{id}  查詢指定商品(單筆)
 * POST  /      新增商品
 * */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ProductController {
	
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
	
	@GetMapping("/{id}")
	public ApiResponse<Product> getProductById(@PathVariable Long id) {
		try {
			Product product = productService.getProductById(id);
			return ApiResponse.success("查詢成功", product);
		} catch (Exception e) {
			return ApiResponse.error("查詢失敗: " + e.getMessage());
		}
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<Product> addProduct(@RequestBody Product product) {
		try {
			Product savedProduct = productService.saveProduct(product);
			return ApiResponse.success("新增成功", savedProduct);
		} catch (Exception e) {
			return ApiResponse.error("新增失敗: " + e.getMessage());
		}
	}
	
	
}




