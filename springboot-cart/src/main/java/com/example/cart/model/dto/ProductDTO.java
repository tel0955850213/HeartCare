package com.example.cart.model.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import com.example.cart.model.dto.ProductImageDTO;

@Data
public class ProductDTO {
	private Long id;
	private String name;
	private String description;
	private BigDecimal price;
	private Integer stock;
	private String imageUrl;
}
