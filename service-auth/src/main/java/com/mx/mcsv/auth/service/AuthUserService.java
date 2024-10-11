package com.mx.mcsv.auth.service;

import java.util.Map;

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
import com.mx.mcsv.auth.dto.LoginUserDTO;
import com.mx.mcsv.auth.dto.TokenDto;
import com.mx.mcsv.auth.dto.UserDTO;
import com.mx.mcsv.auth.dto.UserResponseDTO;
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

	public <T> ApiResponse<TokenDto, Object> login(LoginUserDTO dto) throws AuthException {

		try {
			ResponseEntity<ApiResponse<T, Object>> responseUserEmail = restTemplate.getForEntity(
					"http://service-user/api/users/get-by-email/{email}",
					(Class<ApiResponse<T, Object>>) (Class<?>) ApiResponse.class, dto.getEmail());

			ApiResponse<T, Object> apiResponse = responseUserEmail.getBody();
			String json = apiResponse.getData().toString();

			UserResponseDTO userResponseDTO = convertToUserResponseDTO(json);

			if (!passwordEncoder.matches(dto.getPassword(), userResponseDTO.getPassword())) {
				System.out.println("Password incorrecta");
				throw new AuthException("Password incorrect", HttpStatus.BAD_REQUEST);
			}

			String token = jwtProvider.createToken(userResponseDTO);
			TokenDto tokenDto = new TokenDto(token);

			return new ApiResponse<>(HttpStatus.OK.value(), tokenDto, null);

		} catch (HttpClientErrorException e) {
			String errorMessage = e.getMessage();
			Object errorDetails = extractError(errorMessage);

			return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), null, errorDetails);
		}
	}

	public <T> ApiResponse<T, Object> save(UserDTO dto) throws AuthException {

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

	public <T> TokenDto validate(String token) {
		if (!jwtProvider.validate(token)) {
			System.out.println("***** No es valido");
			return null;
		}
		String email = jwtProvider.getEmailFromToken(token);

		ResponseEntity<ApiResponse<T, Object>> responseUserEmail = restTemplate.getForEntity(
				"http://service-user/api/users/get-by-email/{email}",
				(Class<ApiResponse<T, Object>>) (Class<?>) ApiResponse.class, email);

		ApiResponse<T, Object> apiResponse = responseUserEmail.getBody();
		String json = apiResponse.getData().toString();

		UserResponseDTO userResponseDTO = convertToUserResponseDTO(json);

		if (!userResponseDTO.getEmail().equals(email)) {
			System.out.println("No es el mismo email fallo token");
			return null;
		}
		return new TokenDto(token);
	}

	private UserResponseDTO convertToUserResponseDTO(String json) {
		UserResponseDTO userResponseDTO = new UserResponseDTO();
		userResponseDTO.setId(Long.parseLong(getValue(json, "id")));
		userResponseDTO.setName(getValue(json, "name"));
		userResponseDTO.setUsername(getValue(json, "username"));
		userResponseDTO.setEmail(getValue(json, "email"));
		userResponseDTO.setPassword(getValue(json, "password"));
		return userResponseDTO;
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

	private String getValue(String input, String key) {
		String searchKey = key + "=";
		int startIndex = input.indexOf(searchKey) + searchKey.length();
		int endIndex = input.indexOf(",", startIndex);

		if (endIndex == -1) {
			endIndex = input.indexOf("}", startIndex);
		}

		String value = input.substring(startIndex, endIndex).trim();
		return value;
	}
}