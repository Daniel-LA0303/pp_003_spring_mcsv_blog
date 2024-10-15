package com.mx.mcsv.blog.exception;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mx.mcsv.blog.dto.ApiResponse;

@RestControllerAdvice
public class GlobalException {

	@ExceptionHandler(BlogException.class)
	public ResponseEntity<?> handleUserException(BlogException ex) {
		ErrorDetail errorDetail = new ErrorDetail("Blog Error", ex.getMessage(), LocalDateTime.now());

		ApiResponse<String, ErrorDetail> response = new ApiResponse<>(ex.getStatus().value(), null, errorDetail);

		return new ResponseEntity<>(response, ex.getStatus());
	}

}
