package com.example.cart.service;

import java.util.List;
import java.util.Optional;

import com.example.cart.model.dto.UserDTO;
import com.example.cart.model.dto.LoginDTO;
import com.example.cart.model.entity.User;

public interface UserService {
	UserDTO saveUser(UserDTO userDTO);
	User saveUser(User user);
	Optional<UserDTO> findByUsername(String username);
	Optional<UserDTO> findByEmail(String email);
	List<UserDTO> getAllUsers();
	Optional<UserDTO> findById(Long id);
	User register(User user) throws Exception;
	UserDTO login(LoginDTO loginDTO);
	
	// 添加儀表板所需的方法
	long getTotalUsers();
}
