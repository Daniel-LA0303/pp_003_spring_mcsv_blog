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
import com.mx.mcsv.auth.dto.AuthUserDto;
import com.mx.mcsv.auth.dto.NewUserDto;
import com.mx.mcsv.auth.dto.TokenDto;
import com.mx.mcsv.auth.exceptions.AuthException;
import com.mx.mcsv.auth.service.AuthUserService;

@RestController
@RequestMapping("/auth")
public class AuthUserController {

	@Autowired
	AuthUserService authUserService;

	@PostMapping("/create")
	public ResponseEntity<?> create(@Valid @RequestBody NewUserDto dto, BindingResult result) throws AuthException {
		if (result.hasErrors()) {
			return validation(result);
		}

		ApiResponse<Object, Object> apiResponse = authUserService.save(dto);

		return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody AuthUserDto dto, BindingResult result) throws AuthException {

		if (result.hasErrors()) {
			return validation(result);
		}
		TokenDto tokenDto = authUserService.login(dto);

		ApiResponse<TokenDto, String> response = new ApiResponse<>(200, tokenDto, null);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PostMapping("/validate")
	public ResponseEntity<TokenDto> validate(@RequestParam String token) {
		TokenDto tokenDto = authUserService.validate(token);
		if (tokenDto == null) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(tokenDto);
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
