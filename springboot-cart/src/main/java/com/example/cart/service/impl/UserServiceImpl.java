package com.example.cart.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.cart.model.dto.UserDTO;
import com.example.cart.model.entity.User;
import com.example.cart.model.entity.UserRole;
import com.example.cart.model.entity.Product;
import com.example.cart.model.entity.FavoriteProduct;
import com.example.cart.repository.ProductRepository;
import com.example.cart.repository.UserRepository;
import com.example.cart.service.UserService;
import com.example.cart.model.dto.ProductDTO;
import com.example.cart.model.dto.FavoriteProductDTO;
import com.example.cart.model.dto.FavoriteUserDTO;
import com.example.cart.model.dto.LoginDTO;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public UserDTO saveUser(UserDTO userDTO) {
		User user = modelMapper.map(userDTO, User.class);
		user = userRepository.save(user);
		return modelMapper.map(user, UserDTO.class);
	}
	
	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}
	
	@Override
	public Optional<UserDTO> findByUsername(String username) {
		return userRepository.findByUsername(username)
			.map(user -> modelMapper.map(user, UserDTO.class));
	}
	
	@Override
	public Optional<UserDTO> findByEmail(String email) {
		return userRepository.findByEmail(email)
			.map(user -> modelMapper.map(user, UserDTO.class));
	}
	
	@Override
	public List<UserDTO> getAllUsers() {
		return userRepository.findAll().stream()
			.map(user -> modelMapper.map(user, UserDTO.class))
			.collect(Collectors.toList());
	}
	
	@Override
	public Optional<UserDTO> findById(Long id) {
		return userRepository.findById(id)
			.map(user -> modelMapper.map(user, UserDTO.class));
	}
	
	@Override
	public User register(User user) {
		if (userRepository.findByUsername(user.getUsername()).isPresent()) {
			throw new RuntimeException("用戶名已存在");
		}
		
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new RuntimeException("郵箱已被使用");
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
	
	@Override
	public UserDTO login(LoginDTO loginDTO) {
		User user = userRepository.findByUsername(loginDTO.getUsername())
			.orElseThrow(() -> new RuntimeException("用戶不存在"));

		if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
			throw new RuntimeException("密碼錯誤");
		}

		return modelMapper.map(user, UserDTO.class);
	}
	
	@Override
	public long getTotalUsers() {
		return userRepository.count();
	}
}
