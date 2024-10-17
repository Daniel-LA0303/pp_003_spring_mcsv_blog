package com.mx.mcsv.auth.exceptions;

import java.time.LocalDateTime;

public class ErrorDetail {

	private String error;

	private String message;

	private LocalDateTime timeStamp;

	/**
	 * @param error
	 * @param message
	 * @param timeStamp
	 */
	public ErrorDetail(String error, String message, LocalDateTime timeStamp) {
		this.error = error;
		this.message = message;
		this.timeStamp = timeStamp;
	}

	/**
	 * return the value of the property error
	 *
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * return the value of the property message
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

}
