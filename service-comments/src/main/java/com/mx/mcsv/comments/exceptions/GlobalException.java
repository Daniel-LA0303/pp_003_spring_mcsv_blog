package com.mx.mcsv.comments.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mx.mcsv.comments.dto.ApiResponse;

@RestControllerAdvice
public class GlobalException {

	@ExceptionHandler(CommentException.class)
	public ResponseEntity<?> handleUserException(CommentException ex) {
		ErrorDetail errorDetail = new ErrorDetail("Comment Error", ex.getMessage(), LocalDateTime.now());

		ApiResponse<String, ErrorDetail> response = new ApiResponse<>(ex.getStatus().value(), null, errorDetail);

		return new ResponseEntity<>(response, ex.getStatus());
	}

}
