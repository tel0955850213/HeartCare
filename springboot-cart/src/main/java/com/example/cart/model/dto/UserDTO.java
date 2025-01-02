package com.example.cart.model.dto;

import lombok.Data;
import com.example.cart.model.entity.UserRole;

@Data
public class UserDTO {
	private Long id;
	private String username;
	private String email;
	private UserRole role;
}
