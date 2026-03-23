package com.example.ChatApplication.jwt;

import com.example.ChatApplication.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

private final JwtService jwtService;
private final UserRepository userRepository;

@Override
protected void doFilterInternal(HttpServletRequest request,
								HttpServletResponse response,
								FilterChain filterChain)
	throws ServletException, IOException {

	//skip authentication endpoints
	if(request.getServletPath().startsWith("/auth")){
		filterChain.doFilter(request, response);
		return;
	}

	final String authHeader = request.getHeader("Authorization");

	//No token -> continue
	if(authHeader == null || !authHeader.startsWith("Bearer ")){
		filterChain.doFilter(request,response);
		return;
	}

	try {
		String jwt = authHeader.substring(7);
		String username = jwtService.extractUsername(jwt);

		if(username != null &&
				SecurityContextHolder.getContext().getAuthentication() == null){

			var user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

			if(user != null && jwtService.isTokenValid(jwt, user)){
				UsernamePasswordAuthenticationToken authToken =
						new UsernamePasswordAuthenticationToken(
								user,
								null,
								user.getAuthorities()
						);

				authToken.setDetails(
						new WebAuthenticationDetailsSource()
								.buildDetails(request)
				);

				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
	} catch (Exception e){
		System.out.println("Invalid JWT:" + e.getMessage());
	}
	filterChain.doFilter(request,response);
}

}
