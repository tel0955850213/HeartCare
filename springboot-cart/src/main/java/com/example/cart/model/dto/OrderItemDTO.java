package com.example.cart.model.dto;

import java.math.BigDecimal;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
public class OrderItemDTO {
	private Long id;
	private Long productId;
	private Integer quantity;
	private BigDecimal price;
	private String productName;
	private String productDescription;
}
