package com.example.cart.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.cart.model.dto.UserDTO;
import com.example.cart.model.entity.User;
import com.example.cart.model.entity.UserRole;
import com.example.cart.repository.UserRepository;
import com.example.cart.service.AuthService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public UserDTO login(String username, String password) {
        log.info("嘗試登入: username={}", username);
        
        try {
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用戶不存在"));
            
            log.info("找到用戶: {}, 角色: {}", user.getUsername(), user.getRole());
            
            if (password.equals(user.getPassword())) {
                UserDTO userDTO = new UserDTO();
                userDTO.setId(user.getId());
                userDTO.setUsername(user.getUsername());
                userDTO.setEmail(user.getEmail());
                userDTO.setRole(user.getRole());
                log.info("密碼驗證成功，返回用戶信息: {}", userDTO);
                return userDTO;
            }
            
            log.error("密碼錯誤: username={}", username);
            throw new RuntimeException("密碼錯誤");
        } catch (Exception e) {
            log.error("登入過程出錯: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public UserDTO register(String username, String password, String email, String role) {
        // 檢查用戶名是否已存在
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("用戶名已存在");
        }

        // 創建新用戶
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setRole(UserRole.valueOf(role));

        // 保存用戶
        User savedUser = userRepository.save(user);

        // 轉換為 DTO 並返回
        UserDTO userDTO = new UserDTO();
        userDTO.setId(savedUser.getId());
        userDTO.setUsername(savedUser.getUsername());
        userDTO.setEmail(savedUser.getEmail());
        userDTO.setRole(savedUser.getRole());
        
        return userDTO;
    }
} 