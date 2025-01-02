package com.example.cart.service;

import java.util.List;
import java.util.Map;

import com.example.cart.model.dto.OrderDTO;
import com.example.cart.model.dto.OrderItemDTO;
import com.example.cart.model.entity.Order;
import com.example.cart.model.entity.OrderItem;
import com.example.cart.model.entity.OrderStatus;

public interface OrderService {
	List<OrderDTO> findOrdersByUserId(Long userId);
	OrderDTO saveOrder(Long userId, List<OrderItemDTO> items);
	List<OrderDTO> getAllOrders();
	OrderDTO updateOrderStatus(Long orderId, OrderStatus status);
	
	// 銷售統計相關方法
	double calculateTotalSales();
	Map<String, Double> calculateMonthlySales();
	List<Map<String, Object>> getTopSellingProducts();
	
	// 訂單管理相關方法
	Order createOrder(List<OrderItem> orderItems, Long userId);
	OrderDTO requestCancelOrder(Long orderId, Long userId, String reason);
	OrderDTO updateShippingInfo(Long orderId, String recipientName, String shippingAddress);
	
	// 儀表板統計
	long getTotalOrders();
	double getMonthlyRevenue();
	
	void deleteOrder(Long orderId);
	
	OrderDTO getOrderById(Long orderId);
}
