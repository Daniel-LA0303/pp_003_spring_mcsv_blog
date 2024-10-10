package com.mx.mcsv.auth.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.mx.mcsv.auth.dto.ApiResponse;

@RestControllerAdvice
public class GlobalException {

	@ExceptionHandler(AuthException.class)
	public ResponseEntity<?> handleUserException(AuthException ex) {
		ErrorDetail errorDetail = new ErrorDetail("Auth Error", ex.getMessage(), LocalDateTime.now());

		ApiResponse<String, ErrorDetail> response = new ApiResponse<>(ex.getStatus().value(), null, errorDetail);

		return new ResponseEntity<>(response, ex.getStatus());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDetail> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException me,
			WebRequest req) {
		String error = me.getBindingResult().getFieldError().getDefaultMessage();
		ErrorDetail err = new ErrorDetail("Validation Error", error, LocalDateTime.now());
		return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ErrorDetail> noHandlerFoundExceptionHandler(NoHandlerFoundException e, WebRequest req) {
		ErrorDetail err = new ErrorDetail(e.getMessage(), req.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
	}
}
