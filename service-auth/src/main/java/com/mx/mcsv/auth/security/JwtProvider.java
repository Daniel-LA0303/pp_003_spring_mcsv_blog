package com.mx.mcsv.auth.security;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mx.mcsv.auth.dto.UserResponseDTO;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {
	@Value("${jwt.secret}")
	private String secret;

	public String createToken(UserResponseDTO userResponseDTO) {
		Map<String, Object> claims = new HashMap<>();
		claims = Jwts.claims().setSubject(userResponseDTO.getEmail());
		claims.put("username", userResponseDTO.getUsername());
		Date now = new Date();
		Date exp = new Date(now.getTime() + 3600000);
		return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(exp)
				.signWith(SignatureAlgorithm.HS256, secret).compact();
	}

	public String getEmailFromToken(String token) {
		try {
			return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
		} catch (Exception e) {
			return "bad token";
		}
	}

	public boolean validate(String token) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@PostConstruct
	protected void init() {
		secret = Base64.getEncoder().encodeToString(secret.getBytes());
	}
}