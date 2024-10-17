package com.mx.mcsv.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.mx.mcsv.auth.dto.UserResponseDTO;
import com.mx.mcsv.auth.security.JwtProvider;

@SpringBootTest
@ActiveProfiles("test") // Utiliza el perfil de prueba para cargar properties
public class JwtProviderTest {

	@Autowired
	private JwtProvider jwtProvider;

	private UserResponseDTO userResponseDTO;

	@BeforeEach
	void setUp() {
		userResponseDTO = new UserResponseDTO();
		userResponseDTO.setEmail("test@example.com");
		userResponseDTO.setUsername("testuser");
	}

	@Test
	void testCreateToken() {
		String token = jwtProvider.createToken(userResponseDTO);
		assertTrue(jwtProvider.validate(token), "The token should be valid");
		String email = jwtProvider.getEmailFromToken(token);
		assertEquals("test@example.com", email, "The email extracted from the token should match");
	}

	@Test
	void testGetEmailFromBadToken() {
		String email = jwtProvider.getEmailFromToken("bad.token");
		assertEquals("bad token", email, "The invalid token should return 'bad token'");
	}

	@Test
	void testValidateBadToken() {
		boolean isValid = jwtProvider.validate("bad.token");
		assertTrue(!isValid, "The invalid token should be considered invalid");
	}

}