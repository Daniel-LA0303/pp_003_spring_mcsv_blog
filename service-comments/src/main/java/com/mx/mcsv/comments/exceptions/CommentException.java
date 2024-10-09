package com.mx.mcsv.comments.exceptions;

import org.springframework.http.HttpStatus;

public class CommentException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final HttpStatus status;

	public CommentException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

}
