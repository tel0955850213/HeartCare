package com.example.cart.model.entity;

public enum OrderStatus {
    PENDING,            // 待處理
    PROCESSING,         // 處理中
    SHIPPED,           // 已出貨
    DELIVERED,         // 已送達
    COMPLETED,         // 已完成
    CANCELLED,         // 已取消
    CANCEL_REQUESTED   // 申請取消中
} 