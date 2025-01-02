package com.example.cart.controller.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.cart.response.ApiResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminUploadController {

    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping("/upload")
    public ApiResponse<String> uploadFile(@RequestParam("image") MultipartFile file) {
        try {
            // 確保上傳目錄存在
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 生成唯一的文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;

            // 保存文件
            Path filePath = Paths.get(uploadPath, filename);
            Files.copy(file.getInputStream(), filePath);

            // 返回文件的訪問路徑
            return ApiResponse.success("上傳成功", "/uploads/" + filename);
        } catch (IOException e) {
            return ApiResponse.error("文件上傳失敗: " + e.getMessage());
        }
    }
} 