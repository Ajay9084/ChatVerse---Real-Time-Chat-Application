package com.example.ChatApplication.service;

import com.example.ChatApplication.dto.LoginRequestDTO;
import com.example.ChatApplication.dto.LoginResponseDTO;
import com.example.ChatApplication.dto.RegisterRequestDTO;
import com.example.ChatApplication.entity.Role;
import com.example.ChatApplication.entity.User;
import com.example.ChatApplication.jwt.JwtService;
import com.example.ChatApplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

//register normal user
	public User registerNormalUser(RegisterRequestDTO registerRequestDTO){

		if(userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()){
			throw new RuntimeException("User already exists");
		}
		Set<Role> roles = new HashSet<>();
		roles.add(Role.USER);

		User user = new User();
		user.setUsername(registerRequestDTO.getUsername());
		user.setEmail(registerRequestDTO.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
		user.setRoles(roles);
		return userRepository.save(user);
	}

	//register admin

	public User registerAdmin(RegisterRequestDTO registerRequestDTO){
		if(userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()){
			throw new RuntimeException("Admin already exists");
		}
		Set<Role> roles = new HashSet<>();
		roles.add(Role.ADMIN);
		roles.add(Role.USER);

		User user = new User();
		user.setUsername(registerRequestDTO.getUsername());
		user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
		user.setEmail(registerRequestDTO.getEmail());
		user.setRoles(roles);

		return userRepository.save(user);
	}

	//Login User

	public LoginResponseDTO login(LoginRequestDTO loginRequestDTO){
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequestDTO.getUsername(),
						loginRequestDTO.getPassword()
				)
		);

		//fetch user from DB
		User user = userRepository.findByUsername(loginRequestDTO.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));

		//generate JWT
		String token = jwtService.generateToken(user);

		Set<String> roles = user.getRoles()
				.stream()
				.map(Role::name)
				.collect(Collectors.toSet());
		return LoginResponseDTO.builder()
				.token(token)
				.username(user.getUsername())
				.roles(roles)
				.build();
	}
}
