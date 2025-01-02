package com.example.cart.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import com.example.cart.model.entity.OrderStatus;

@Data
public class OrderDTO {
	private Long id;
	private Long userId;
	private LocalDateTime orderDate;
	private OrderStatus status;
	private BigDecimal totalAmount;
	private String recipientName;
	private String shippingAddress;
	private List<OrderItemDTO> items;
}
