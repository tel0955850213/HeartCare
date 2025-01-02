package com.example.cart.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "product_image")
public class ProductImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;
	
	@Column(nullable = false)
	private String imageUrl;  // 圖片URL
	
	@Column(name = "display_order")
	private Integer displayOrder;  // 顯示順序
	
	@Column(name = "is_primary")
	private Boolean isPrimary;  // 是否為主圖
	
	@Column(length = 500)
	private String description;  // 圖片描述
}








