package com.mx.mcsv.auth.dto;

public class AuthUserDto {

	private String userName;

	private String password;

	/**
	 * 
	 */
	public AuthUserDto() {
	}

	/**
	 * @param userName
	 * @param password
	 */
	public AuthUserDto(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	/**
	 * return the value of the property password
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * return the value of the property userName
	 *
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * set the value of the property password
	 *
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * set the value of the property userName
	 *
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
