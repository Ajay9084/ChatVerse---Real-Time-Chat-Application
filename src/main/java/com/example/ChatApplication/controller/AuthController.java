package com.example.ChatApplication.controller;

import com.example.ChatApplication.dto.LoginRequestDTO;
import com.example.ChatApplication.dto.LoginResponseDTO;
import com.example.ChatApplication.dto.RegisterRequestDTO;
import com.example.ChatApplication.entity.User;
import com.example.ChatApplication.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/register")
public ResponseEntity<User> registerNormalUser(@RequestBody RegisterRequestDTO registerRequestDTO){
return ResponseEntity.ok(authService.registerNormalUser(registerRequestDTO));
}

    @PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
		return ResponseEntity.ok(authService.login(loginRequestDTO));
	}
}
