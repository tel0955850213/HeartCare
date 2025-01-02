package com.example.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.cart.model.entity.OrderItem;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    void deleteByOrderId(Long orderId);
}
