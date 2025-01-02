package com.example.cart.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.cart.model.entity.Product;
import com.example.cart.repository.ProductRepository;
import com.example.cart.service.BookService;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Override
    public Product getRecommendationByType(String type) {
        // 直接根據性格類型返回指定的書籍
        switch (type.toUpperCase()) {
            case "EXPLORER":
                // 探險家推薦《哈利波特：神秘的魔法石》
                return productRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("找不到推薦書籍"));
                
            case "DREAMER":
                // 夢想家推薦《世界冰冷，哲學是篝火》
                return productRepository.findById(2L)
                    .orElseThrow(() -> new RuntimeException("找不到推薦書籍"));
                
            case "THINKER":
                // 思考者推薦《吳若權，金剛經讀寫組合》
                return productRepository.findById(3L)
                    .orElseThrow(() -> new RuntimeException("找不到推薦書籍"));
                
            default:
                throw new RuntimeException("無效的性格類型");
        }
    }
} 