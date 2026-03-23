package com.example.ChatApplication.service;

import com.example.ChatApplication.entity.User;
import com.example.ChatApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
private UserRepository userRepository;

public User getUserByUsername(String username){
	return userRepository.findByUsername(username)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));
}
}
