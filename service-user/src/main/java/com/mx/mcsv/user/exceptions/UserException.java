package com.mx.mcsv.user.exceptions;

import org.springframework.http.HttpStatus;

public class UserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final HttpStatus status;

	public UserException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

}
