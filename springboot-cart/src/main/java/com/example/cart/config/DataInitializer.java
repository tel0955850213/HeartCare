package com.example.cart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.cart.model.entity.Product;
import com.example.cart.model.entity.User;
import com.example.cart.model.entity.UserRole;
import com.example.cart.repository.ProductRepository;
import com.example.cart.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 初始化測試用戶
        if (userRepository.count() == 0) {
            User testUser = new User();
            testUser.setUsername("test");
            testUser.setPassword(passwordEncoder.encode("test123"));
            testUser.setEmail("test@example.com");
            testUser.setRole(UserRole.USER);
            userRepository.save(testUser);
            
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setEmail("admin@example.com");
            adminUser.setRole(UserRole.ADMIN);
            userRepository.save(adminUser);
            
            System.out.println("測試用戶初始化完成！");
        }
        
        // 檢查是否已有資料
        if (productRepository.count() == 0) {
            // 建立測試商品資料
            Product[] products = {
                createProduct(
                    "哈利波特：神秘的魔法石",
                    "哈利波特系列第一集，講述年輕巫師的奇幻冒險",
                    new BigDecimal("450"),
                    "/images/books/book1.jpg"
                ),
                createProduct(
                    "魔戒首部曲：魔戒現身",
                    "100句清晰通透哲學名言，解答生活的所有疑惑與困惑",
                    new BigDecimal("520"),
                    "/images/books/book2.jpg"
                ),
                createProduct(
                    "小王子",
                    "經典童話故事，探討愛與人性的永恆經典",
                    new BigDecimal("280"),
                    "/images/books/book3.jpg"
                ),
                createProduct(
                    "納尼亞傳奇",
                    "奇幻文學經典，穿越衣櫥的冒險故事",
                    new BigDecimal("380"),
                    "/images/books/book4.jpg"
                ),
                createProduct(
                    "龍族",
                    "你不是在獨處，只要用對方法，羞澀能自在相伴",
                    new BigDecimal("420"),
                    "/images/books/book5.jpg"
                ),
                createProduct(
                    "獵魔士",
                    "獵魔士系列小說，改編自暢銷遊戲",
                    new BigDecimal("499"),
                    "/images/books/book6.jpg"
                )
            };

            // 儲存所有商品
            productRepository.saveAll(Arrays.asList(products));
            
            System.out.println("書籍資料初始化完成！");
        }
    }

    private Product createProduct(String name, String description, BigDecimal price, String imageUrl) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setImageUrl(imageUrl);
        product.setStock(100); // 設定初始庫存
        return product;
    }
} 