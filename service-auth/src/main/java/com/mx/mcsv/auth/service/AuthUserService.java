package com.mx.mcsv.auth.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mx.mcsv.auth.dto.ApiResponse;
import com.mx.mcsv.auth.dto.AuthUserDto;
import com.mx.mcsv.auth.dto.NewUserDTO;
import com.mx.mcsv.auth.dto.TokenDto;
import com.mx.mcsv.auth.entity.AuthUser;
import com.mx.mcsv.auth.exceptions.AuthException;
import com.mx.mcsv.auth.repository.AuthUserRepository;
import com.mx.mcsv.auth.security.JwtProvider;

@Service
public class AuthUserService {

	@Autowired
	AuthUserRepository authUserRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	JwtProvider jwtProvider;

	private final ObjectMapper objectMapper = new ObjectMapper();

	public TokenDto login(AuthUserDto dto) throws AuthException {

		Optional<AuthUser> user = authUserRepository.findByUserName(dto.getUserName());
		if (!user.isPresent()) {
			throw new AuthException("Username not found", HttpStatus.NOT_FOUND);
		}

		if (!passwordEncoder.matches(dto.getPassword(), user.get().getPassword())) {
			throw new AuthException("Password incorrect", HttpStatus.BAD_REQUEST);
		}
		return new TokenDto(jwtProvider.createToken(user.get()));
	}

	public <T> ApiResponse<T, Object> save(NewUserDTO dto) throws AuthException {

		try {

			String password = passwordEncoder.encode(dto.getPassword());

			dto.setPassword(password);

			ResponseEntity<ApiResponse<T, Object>> response = restTemplate.postForEntity(
					"http://service-user/api/users/new-user", dto,
					(Class<ApiResponse<T, Object>>) (Class<?>) ApiResponse.class);

			return response.getBody();

		} catch (HttpClientErrorException e) {
			String errorMessage = e.getMessage();

			Object errorDetails = extractError(errorMessage);

			return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), null, errorDetails);
		}

	}

	public TokenDto validate(String token) {
		if (!jwtProvider.validate(token)) {
			return null;
		}
		String username = jwtProvider.getUserNameFromToken(token);
		if (!authUserRepository.findByUserName(username).isPresent()) {
			return null;
		}
		return new TokenDto(token);
	}

	private Object extractError(String errorResponse) {
		try {

			String cleanedErrorResponse = errorResponse.substring(errorResponse.indexOf("{"),
					errorResponse.lastIndexOf("}") + 1);

			Map<String, Object> errorMap = objectMapper.readValue(cleanedErrorResponse, Map.class);

			if (errorMap.containsKey("error")) {
				return errorMap.get("error");
			} else {
				return Map.of("error", "No error information found");
			}
		} catch (JsonProcessingException e) {
			return Map.of("error", "Failed to parse error response");
		}
	}
}