package com.mx.mcsv.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mx.mcsv.user.dto.ApiResponse;
import com.mx.mcsv.user.dto.UserDTO;
import com.mx.mcsv.user.entity.User;
import com.mx.mcsv.user.exceptions.UserException;
import com.mx.mcsv.user.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) throws UserException {
		userService.deleteUser(id);

		ApiResponse<String, String> apiResponse = new ApiResponse<>(200, "User deleted", null);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<?> getAllUsers() {
		List<UserDTO> users = userService.findAll();

		ApiResponse<List<UserDTO>, String> apiResponse = new ApiResponse<>(200, users, null);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getUser(@PathVariable Long id) throws UserException {
		UserDTO userDTO = userService.findById(id);
		ApiResponse<UserDTO, String> response = new ApiResponse<>(200, userDTO, null);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> saveUser(@Valid @RequestBody User user, BindingResult result) throws UserException {

		if (result.hasErrors()) {
			return validation(result);
		}

		UserDTO userDTO = userService.save(user);
		ApiResponse<UserDTO, String> response = new ApiResponse<>(201, userDTO, null);
		return new ResponseEntity<>(response, HttpStatus.CREATED);

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody User user, BindingResult result)
			throws UserException {

		if (result.hasErrors()) {
			return validation(result);
		}

		UserDTO updatedUser = userService.update(id, user);

		ApiResponse<UserDTO, String> response = new ApiResponse<>(200, updatedUser, null);
		return new ResponseEntity<>(response, HttpStatus.OK);
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