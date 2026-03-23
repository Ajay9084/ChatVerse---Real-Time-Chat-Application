package com.example.ChatApplication.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {
//Generate Token 🔐
//Extract Data from Token 📦
//Validate Token ✅

	@Value("${jwt.secretkey}")
	private String secretKey;

	@Value("${jwt.expiration}")
	private Long jwtExpiration;

	public String extractUsername(String jwtToken){
		return extractClaim(jwtToken, Claims::getSubject);
	}

	private <T> T extractClaim(String jwtToken, Function<Claims, T> claimResolver){
		final Claims claims = extractAllClaim(jwtToken);
		return claimResolver.apply(claims);
	}

	private Claims extractAllClaim(String jwtToken){
		return Jwts.parser()
				.verifyWith(getSignInKey())
				.build()
				.parseSignedClaims(jwtToken)
				.getPayload();
	}

	public SecretKey getSignInKey(){
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	public String generateToken(UserDetails userDetails) {
		List<String> roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)  // → "ROLE_ADMIN"
				.collect(Collectors.toList());

		Map<String, Object> extraClaims = new HashMap<>();
		extraClaims.put("roles", roles);

		return generateToken(extraClaims, userDetails); // now has roles!
	}

	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
		return Jwts
				.builder()
				.claims(extraClaims)
				.subject(userDetails.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + jwtExpiration))
				.signWith(getSignInKey())
				.compact();
	}

	public boolean isTokenValid(String jwtToken, UserDetails userDetails){
		final String username = extractUsername(jwtToken);

		return (userDetails.getUsername().equals(username) && !isTokenExpired(jwtToken));
	}

	private boolean isTokenExpired(String jwtToken){
		return extractExpiration(jwtToken).before(new Date());
	}

	private Date extractExpiration(String jwtToken){
		return extractClaim(jwtToken, Claims::getExpiration);
	}
}
