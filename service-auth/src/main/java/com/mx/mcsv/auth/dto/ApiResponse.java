package com.mx.mcsv.auth.dto;

import java.time.LocalDateTime;

public class ApiResponse<T, M> {
	private int status;
	private T data;
	private M error;
	private LocalDateTime timeStamp;

	/**
	 * 
	 */
	public ApiResponse() {
	}

	/**
	 * @param status
	 * @param data
	 * @param error
	 * @param timeStamp
	 */
	public ApiResponse(int status, T data, M error) {
		this.status = status;
		this.data = data;
		this.error = error;
		this.timeStamp = LocalDateTime.now();
	}

	/**
	 * return the value of the property data
	 *
	 * @return the data
	 */
	public T getData() {
		return data;
	}

	/**
	 * return the value of the property error
	 *
	 * @return the error
	 */
	public M geterror() {
		return error;
	}

	/**
	 * return the value of the property status
	 *
	 * @return the status
	 */
	public int getStatus() {
		return status;
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
	 * set the value of the property data
	 *
	 * @param data the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * set the value of the property error
	 *
	 * @param error the error to set
	 */
	public void seterror(M error) {
		this.error = error;
	}

	/**
	 * set the value of the property status
	 *
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
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
