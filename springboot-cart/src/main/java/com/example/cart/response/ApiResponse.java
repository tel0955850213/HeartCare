package com.example.cart.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
	private int status;
	private String message;
	private T data;

	public static <T> ApiResponse<T> success(String message, T data) {
		return new ApiResponse<>(200, message, data);
	}

	public static <T> ApiResponse<T> error(String message) {
		return new ApiResponse<>(400, message, null);
	}
}
