package com.example.cart.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cart.aop.CheckUserSession;
import com.example.cart.model.dto.OrderDTO;
import com.example.cart.model.dto.OrderItemDTO;
import com.example.cart.model.dto.UserDTO;
import com.example.cart.model.entity.Order;
import com.example.cart.model.entity.OrderStatus;
import com.example.cart.response.ApiResponse;
import com.example.cart.service.OrderService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@GetMapping
	@CheckUserSession
	public ResponseEntity<ApiResponse<List<OrderDTO>>> getAllOrders(HttpSession session) {
		try {
			UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
			if (userDTO == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(ApiResponse.error("請先登入"));
			}
			
			List<OrderDTO> orderDTOs = orderService.findOrdersByUserId(userDTO.getId());
			return ResponseEntity.ok(ApiResponse.success("查詢成功", orderDTOs));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ApiResponse.error("查詢失敗: " + e.getMessage()));
		}
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<OrderDTO>> createOrder(
			@RequestBody List<OrderItemDTO> items,
			HttpSession session) {
		try {
			UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
			if (userDTO == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(ApiResponse.error("請先登入"));
			}

			OrderDTO order = orderService.saveOrder(userDTO.getId(), items);
			return ResponseEntity.ok(ApiResponse.success("訂單建立成功", order));
		} catch (Exception e) {
			return ResponseEntity.badRequest()
				.body(ApiResponse.error("建立訂單失敗: " + e.getMessage()));
		}
	}
	
	@PostMapping("/{orderId}/cancel")
	public ResponseEntity<ApiResponse<OrderDTO>> cancelOrder(
			@PathVariable Long orderId,
			@RequestBody Map<String, String> cancelRequest,
			HttpSession session) {
		try {
			UserDTO user = (UserDTO) session.getAttribute("userDTO");
			if (user == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(ApiResponse.error("請先登入"));
			}

			OrderDTO cancelledOrder = orderService.requestCancelOrder(
				orderId,
				user.getId(),
				cancelRequest.get("reason")
			);

			return ResponseEntity.ok(ApiResponse.success("訂單取消成功", cancelledOrder));
		} catch (Exception e) {
			return ResponseEntity.badRequest()
				.body(ApiResponse.error("取消訂單失敗: " + e.getMessage()));
		}
	}
	
	@PostMapping("/{orderId}/shipping-info")
	public ResponseEntity<ApiResponse<OrderDTO>> updateShippingInfo(
			@PathVariable Long orderId,
			@RequestBody Map<String, String> shippingInfo) {
		try {
			OrderDTO order = orderService.updateShippingInfo(
				orderId,
				shippingInfo.get("recipientName"),
				shippingInfo.get("shippingAddress")
			);
			return ResponseEntity.ok(ApiResponse.success("配送信息已更新", order));
		} catch (Exception e) {
			return ResponseEntity.badRequest()
				.body(ApiResponse.error(e.getMessage()));
		}
	}
	
	@PutMapping("/{id}/status")
	public ApiResponse<OrderDTO> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> statusMap) {
		try {
			OrderStatus status = OrderStatus.valueOf(statusMap.get("status"));
			OrderDTO updatedOrder = orderService.updateOrderStatus(id, status);
			return ApiResponse.success("訂單狀態更新成功", updatedOrder);
		} catch (IllegalArgumentException e) {
			return ApiResponse.error("無效的訂單狀態");
		} catch (Exception e) {
			return ApiResponse.error(e.getMessage());
		}
	}
}


