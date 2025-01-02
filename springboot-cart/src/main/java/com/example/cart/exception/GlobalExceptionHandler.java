package com.example.cart.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.cart.response.ApiResponse;
import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ApiResponse<?> handleProductNotFoundException(ProductNotFoundException e) {
        return ApiResponse.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception e, Locale locale) {
        return ApiResponse.error(e.getMessage());
    }
}
