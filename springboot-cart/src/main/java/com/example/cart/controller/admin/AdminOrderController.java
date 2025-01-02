package com.example.cart.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.cart.model.dto.OrderDTO;
import com.example.cart.model.entity.OrderStatus;
import com.example.cart.response.ApiResponse;
import com.example.cart.service.OrderService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/admin/orders")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ApiResponse<List<OrderDTO>> getAllOrders() {
        try {
            log.info("獲取所有訂單");
            List<OrderDTO> orders = orderService.getAllOrders()
                .stream()
                .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
                .collect(Collectors.toList());
            return ApiResponse.success("查詢成功", orders);
        } catch (Exception e) {
            log.error("獲取訂單列表失敗", e);
            return ApiResponse.error("獲取訂單列表失敗: " + e.getMessage());
        }
    }

    @PutMapping("/{orderId}/status")
    public ApiResponse<OrderDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody Map<String, String> statusMap) {
        try {
            OrderStatus status = OrderStatus.valueOf(statusMap.get("status"));
            OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, status);
            return ApiResponse.success("訂單狀態更新成功", updatedOrder);
        } catch (Exception e) {
            log.error("更新訂單狀態失敗", e);
            return ApiResponse.error("更新失敗: " + e.getMessage());
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long orderId) {
        try {
            // 檢查訂單狀態
            OrderDTO order = orderService.getOrderById(orderId);
            if (order.getStatus() != OrderStatus.CANCELLED) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("只能刪除已取消的訂單"));
            }
            
            orderService.deleteOrder(orderId);
            return ResponseEntity.ok(ApiResponse.success("訂單刪除成功", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("刪除訂單失敗: " + e.getMessage()));
        }
    }
} 