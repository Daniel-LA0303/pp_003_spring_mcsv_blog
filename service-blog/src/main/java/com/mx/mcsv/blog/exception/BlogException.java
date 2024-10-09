package com.mx.mcsv.blog.exception;

import org.springframework.http.HttpStatus;

public class BlogException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final HttpStatus status;

	public BlogException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

}