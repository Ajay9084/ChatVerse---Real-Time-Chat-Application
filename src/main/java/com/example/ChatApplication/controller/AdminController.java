package com.example.ChatApplication.controller;

import com.example.ChatApplication.dto.RegisterRequestDTO;
import com.example.ChatApplication.entity.Role;
import com.example.ChatApplication.entity.User;
import com.example.ChatApplication.repository.ChatMessageRepository;
import com.example.ChatApplication.repository.UserRepository;
import com.example.ChatApplication.service.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ChatMessageRepository chatMessageRepository;

	@Autowired
	private AuthService authService;

	@PostMapping("/register-admin")
	public ResponseEntity<User> registerAdmin(@RequestBody RegisterRequestDTO registerRequestDTO) {
		return ResponseEntity.ok(authService.registerAdmin(registerRequestDTO));
	}

	@GetMapping("/users")
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Transactional
	@DeleteMapping("/user/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found"));

		// Delete all messages sent or received by this user first
		// to avoid foreign key constraint violation
		chatMessageRepository.deleteBySender(user);
		chatMessageRepository.deleteByReceiver(user);

		userRepository.delete(user);
		return ResponseEntity.ok("User deleted Successfully");
	}

	@PutMapping("/block/{id}")
	public ResponseEntity<String> blockUser(@PathVariable Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found"));
		user.setBlocked(true);
		userRepository.save(user);
		return ResponseEntity.ok("User blocked successfully");
	}

	@PutMapping("/unblock/{id}")
	public ResponseEntity<String> unblockUser(@PathVariable Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found"));
		user.setBlocked(false);
		userRepository.save(user);
		return ResponseEntity.ok("User unblocked successfully");
	}

	@PostMapping("/create-admin")
	public User createAdmin(@RequestBody User user) {
		if (userRepository.findByUsername(user.getUsername()).isPresent()) {
			throw new IllegalArgumentException("Username already exists");
		}
		user.setRoles(Set.of(Role.ADMIN));
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
}
