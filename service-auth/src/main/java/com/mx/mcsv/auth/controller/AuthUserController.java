package com.mx.mcsv.auth.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mx.mcsv.auth.dto.ApiResponse;
import com.mx.mcsv.auth.dto.LoginUserDTO;
import com.mx.mcsv.auth.dto.TokenDto;
import com.mx.mcsv.auth.dto.UserDTO;
import com.mx.mcsv.auth.exceptions.AuthException;
import com.mx.mcsv.auth.service.AuthUserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/auth")
public class AuthUserController {

	@Autowired
	AuthUserService authUserService;

	@PostMapping("/create")
	@CircuitBreaker(name = "userCB", fallbackMethod = "fallBackCreateUser")
	public ResponseEntity<?> create(@Valid @RequestBody UserDTO dto, BindingResult result) throws AuthException {
		if (result.hasErrors()) {
			return validation(result);
		}

		ApiResponse<Object, Object> apiResponse = authUserService.save(dto);

		return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
	}

	@PostMapping("/login")
	@CircuitBreaker(name = "userCB", fallbackMethod = "fallBackLoginUser")
	public ResponseEntity<?> login(@Valid @RequestBody LoginUserDTO dto, BindingResult result) throws AuthException {

		if (result.hasErrors()) {
			return validation(result);
		}
		ApiResponse<TokenDto, Object> response = authUserService.login(dto);

		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));

	}

	@PostMapping("/validate")
	public ResponseEntity<TokenDto> validate(@RequestParam String token) {
		TokenDto tokenDto = authUserService.validate(token);
		if (tokenDto == null) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(tokenDto);
	}

	private ResponseEntity<?> fallBackCreateUser(UserDTO dto, BindingResult result, Throwable e) {
		ApiResponse<Object, String> response = new ApiResponse<>(HttpStatus.SERVICE_UNAVAILABLE.value(), null,
				"The User could not be created at this time due to an error.");
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	private ResponseEntity<?> fallBackLoginUser(LoginUserDTO dto, BindingResult result, Throwable e) {

		ApiResponse<Object, String> response = new ApiResponse<>(HttpStatus.SERVICE_UNAVAILABLE.value(), null,
				"The User could not be login at this time.");
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	private ResponseEntity<ApiResponse<Map<String, String>, Map<String, String>>> validation(BindingResult result) {
		Map<String, String> errorsMap = new HashMap<>();

		result.getFieldErrors().forEach(err -> {
			errorsMap.put(err.getField(), "The field " + err.getField() + " " + err.getDefaultMessage());
		});

		ApiResponse<Map<String, String>, Map<String, String>> apiResponse = new ApiResponse<>(400, null, errorsMap);
		return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
	}
}
