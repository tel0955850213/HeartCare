package com.example.cart.model.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class FavoriteProductDTO {
	private Long id;
	private String name;
	private String description;
	private BigDecimal price;
	private String imageUrl;
	private Boolean isFavorited;
}
