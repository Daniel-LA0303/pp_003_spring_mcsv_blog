package com.mx.mcsv.auth.dto;

public class TokenDto {

	private String token;

	/**
	 * 
	 */
	public TokenDto() {
	}

	/**
	 * @param token
	 */
	public TokenDto(String token) {
		this.token = token;
	}

	/**
	 * return the value of the property token
	 *
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * set the value of the property token
	 *
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

}