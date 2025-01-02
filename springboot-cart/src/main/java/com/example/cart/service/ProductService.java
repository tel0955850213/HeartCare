package com.example.cart.service;

import java.util.List;
import com.example.cart.model.entity.Product;

public interface ProductService {
	List<Product> getAllProducts();
	Product saveProduct(Product product);
	Product updateProduct(Product product);
	void deleteProduct(Long id);
	Product getProductById(Long id);
	long getTotalProducts();
}
