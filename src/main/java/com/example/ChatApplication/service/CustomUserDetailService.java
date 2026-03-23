package com.example.ChatApplication.service;

import com.example.ChatApplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService{


	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		com.example.ChatApplication.entity.User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("user not found"));
	return new org.springframework.security.core.userdetails.User(
			user.getUsername(),
			user.getPassword(),
			user.getRoles().stream()
					.map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
					.toList()
	);
	}
}
