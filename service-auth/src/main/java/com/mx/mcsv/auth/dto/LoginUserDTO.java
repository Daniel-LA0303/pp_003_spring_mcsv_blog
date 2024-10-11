package com.mx.mcsv.auth.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class LoginUserDTO {

	@NotBlank
	private String email;

	@NotEmpty
	private String password;

	/**
	 * 
	 */
	public LoginUserDTO() {
	}

	/**
	 * @param email
	 * @param password
	 */
	public LoginUserDTO(String email, String password) {
		this.email = email;
		this.password = password;
	}

	/**
	 * return the value of the property email
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
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
	 * set the value of the property email
	 *
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * set the value of the property password
	 *
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
