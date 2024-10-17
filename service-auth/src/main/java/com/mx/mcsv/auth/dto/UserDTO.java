package com.mx.mcsv.auth.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class UserDTO {

	@NotBlank(message = "The field name must not be blank")
	private String name;

	@NotBlank(message = "The field username must not be blank")
	private String username;

	@NotBlank(message = "The field email must not be blank")
	private String email;

	@NotEmpty(message = "The field password must not be null")
	private String password;

	/**
	 * 
	 */
	public UserDTO() {
	}

	/**
	 * @param name
	 * @param username
	 * @param email
	 * @param password
	 */
	public UserDTO(String name, String username, String email, String password) {
		this.name = name;
		this.username = username;
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
	 * return the value of the property name
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
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
	 * return the value of the property username
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
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
	 * set the value of the property name
	 *
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * set the value of the property username
	 *
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

}
