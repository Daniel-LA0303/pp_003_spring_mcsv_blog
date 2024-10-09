package com.mx.mcsv.comments.exceptions;

import java.time.LocalDateTime;

public class ErrorDetail {

	private String error;

	private String message;

	private LocalDateTime timeStamp;

	/**
	 * 
	 */
	public ErrorDetail() {
	}

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

	/**
	 * return the value of the property timeStamp
	 *
	 * @return the timeStamp
	 */
	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	/**
	 * set the value of the property error
	 *
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * set the value of the property message
	 *
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * set the value of the property timeStamp
	 *
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

}
