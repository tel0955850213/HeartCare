package com.example.cart.service.impl;

import java.time.LocalDateTime;
import java.util.*;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cart.model.dto.OrderDTO;
import com.example.cart.model.dto.OrderItemDTO;
import com.example.cart.model.entity.Order;
import com.example.cart.model.entity.OrderItem;
import com.example.cart.model.entity.OrderStatus;
import com.example.cart.model.entity.Product;
import com.example.cart.repository.OrderRepository;
import com.example.cart.repository.ProductRepository;
import com.example.cart.repository.OrderItemRepository;
import com.example.cart.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<OrderDTO> findOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
            .map(this::convertToDTO)
            .toList();
    }

    @Override
    @Transactional
    public OrderDTO saveOrder(Long userId, List<OrderItemDTO> items) {
        // 檢查參數
        if (items == null || items.isEmpty()) {
            throw new RuntimeException("購物車是空的");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemDTO item : items) {
            Product product = productRepository.findById(item.getProductId())
                .orElseThrow(() -> new RuntimeException("商品不存在"));
            
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        
        order.setTotalAmount(totalAmount);
        order = orderRepository.save(order);
        
        for (OrderItemDTO item : items) {
            Product product = productRepository.findById(item.getProductId())
                .orElseThrow(() -> new RuntimeException("商品不存在"));
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItemRepository.save(orderItem);
        }
        
        return convertToDTO(order);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
            .map(this::convertToDTO)
            .toList();
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("訂單不存在"));
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }

    @Override
    public double calculateTotalSales() {
        return orderRepository.findAll().stream()
            .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
            .mapToDouble(order -> order.getTotalAmount().doubleValue())
            .sum();
    }

    @Override
    public Map<String, Double> calculateMonthlySales() {
        return orderRepository.findAll().stream()
            .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
            .collect(Collectors.groupingBy(
                order -> YearMonth.from(order.getOrderDate()).toString(),
                Collectors.summingDouble(order -> order.getTotalAmount().doubleValue())
            ));
    }

    @Override
    public List<Map<String, Object>> getTopSellingProducts() {
        Map<Product, Integer> productSales = orderRepository.findAll().stream()
            .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
            .flatMap(order -> order.getItems().stream())
            .collect(Collectors.groupingBy(
                OrderItem::getProduct,
                Collectors.summingInt(OrderItem::getQuantity)
            ));

        return productSales.entrySet().stream()
            .map(entry -> {
                Map<String, Object> productData = new HashMap<>();
                Product product = entry.getKey();
                productData.put("productId", product.getId());
                productData.put("productName", product.getName());
                productData.put("totalSold", entry.getValue());
                return productData;
            })
            .sorted((a, b) -> ((Integer)b.get("totalSold")).compareTo((Integer)a.get("totalSold")))
            .limit(10)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Order createOrder(List<OrderItem> orderItems, Long userId) {
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        
        BigDecimal totalAmount = orderItems.stream()
            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);
        
        for (OrderItem item : orderItems) {
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
        }
        
        return savedOrder;
    }

    @Override
    public OrderDTO requestCancelOrder(Long orderId, Long userId, String reason) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("訂單不存在"));
            
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("無權限取消此訂單");
        }
        
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("只能取消待處理的訂單");
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelReason(reason);
        order.setCancelRequestTime(LocalDateTime.now());
        order.setCancelApprovedTime(LocalDateTime.now());
        
        return convertToDTO(orderRepository.save(order));
    }

    @Override
    public OrderDTO updateShippingInfo(Long orderId, String recipientName, String shippingAddress) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("訂單不存在"));
            
        order.setRecipientName(recipientName);
        order.setShippingAddress(shippingAddress);
        
        return convertToDTO(orderRepository.save(order));
    }

    @Override
    public long getTotalOrders() {
        return orderRepository.count();
    }

    @Override
    public double getMonthlyRevenue() {
        YearMonth currentMonth = YearMonth.now();
        return orderRepository.findAll().stream()
            .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
            .filter(order -> YearMonth.from(order.getOrderDate()).equals(currentMonth))
            .mapToDouble(order -> order.getTotalAmount().doubleValue())
            .sum();
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("訂單不存在"));
        
        if (order.getStatus() != OrderStatus.CANCELLED) {
            throw new RuntimeException("只能刪除已取消的訂單");
        }
        
        // 先刪除訂單項
        orderItemRepository.deleteByOrderId(orderId);
        // 再刪除訂單
        orderRepository.deleteById(orderId);
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("訂單不存在"));
        return convertToDTO(order);
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setRecipientName(order.getRecipientName());
        dto.setShippingAddress(order.getShippingAddress());
        
        if (order.getItems() != null) {
            dto.setItems(order.getItems().stream()
                .map(this::convertToItemDTO)
                .toList());
        }
        
        return dto;
    }

    private OrderItemDTO convertToItemDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setProductName(item.getProduct().getName());
        dto.setProductDescription(item.getProduct().getDescription());
        return dto;
    }
}
