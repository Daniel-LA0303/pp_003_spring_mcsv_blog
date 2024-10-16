package com.mx.mcsv.user.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mx.mcsv.user.dto.ApiResponse;

@RestControllerAdvice
public class GlobalException {

	@ExceptionHandler(UserException.class)
	public ResponseEntity<?> handleUserException(UserException ex) {
		ErrorDetail errorDetail = new ErrorDetail("User Error", ex.getMessage(), LocalDateTime.now());

		ApiResponse<String, ErrorDetail> response = new ApiResponse<>(ex.getStatus().value(), null, errorDetail);

		return new ResponseEntity<>(response, ex.getStatus());
	}

}
