package com.example.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.cart.model.dto.LoginDTO;
import com.example.cart.model.dto.UserDTO;
import com.example.cart.model.dto.RegisterDTO;
import com.example.cart.response.ApiResponse;
import com.example.cart.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ApiResponse<UserDTO> login(@RequestBody LoginDTO loginDTO, HttpSession session) {
        try {
            log.info("收到登入請求: username={}", loginDTO.getUsername());
            
            UserDTO userDTO = authService.login(loginDTO.getUsername(), loginDTO.getPassword());
            session.setAttribute("userDTO", userDTO);
            
            log.info("登入成功，設置 session: {}, 返回用戶信息: {}", session.getId(), userDTO);
            ApiResponse<UserDTO> response = ApiResponse.success("登入成功", userDTO);
            log.info("返回響應: {}", response);
            return response;
            
        } catch (Exception e) {
            log.error("登入失敗: {}", e.getMessage());
            ApiResponse<UserDTO> response = ApiResponse.error(e.getMessage());
            log.info("返回錯誤響應: {}", response);
            return response;
        }
    }

    @GetMapping("/check")
    public ApiResponse<UserDTO> checkLoginStatus(HttpSession session) {
        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        if (userDTO != null) {
            return ApiResponse.success("已登入", userDTO);
        }
        return ApiResponse.error("未登入");
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpSession session) {
        session.invalidate();
        return ApiResponse.success("登出成功", null);
    }

    @PostMapping("/register")
    public ApiResponse<UserDTO> register(@RequestBody RegisterDTO registerDTO) {
        try {
            log.info("收到註冊請求: {}", registerDTO.getUsername());
            UserDTO userDTO = authService.register(
                registerDTO.getUsername(),
                registerDTO.getPassword(),
                registerDTO.getEmail(),
                registerDTO.getRole()
            );
            log.info("註冊成功: {}", userDTO.getUsername());
            return ApiResponse.success("註冊成功", userDTO);
        } catch (Exception e) {
            log.error("註冊失敗: {}", e.getMessage());
            return ApiResponse.error("註冊失敗: " + e.getMessage());
        }
    }
}
