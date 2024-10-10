package com.mx.mcsv.gateway.dto;

public class ApiResponse<T, E> {
	private int statusCode;
	private T data;
	private E errorMessage;

	public ApiResponse(int statusCode, T data, E errorMessage) {
		this.statusCode = statusCode;
		this.data = data;
		this.errorMessage = errorMessage;
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
	 * return the value of the property errorMessage
	 *
	 * @return the errorMessage
	 */
	public E getErrorMessage() {
		return errorMessage;
	}

	/**
	 * return the value of the property statusCode
	 *
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
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
	 * set the value of the property errorMessage
	 *
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(E errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * set the value of the property statusCode
	 *
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

}