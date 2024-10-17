package com.mx.mcsv.auth.utils;

import com.mx.mcsv.auth.dto.UserDTO;

public class UserDTOBuilder {

	/**
	 * Name
	 */
	private String name;

	/**
	 * Username
	 */
	private String username;

	/**
	 * Email
	 */
	private String email;

	/**
	 * Password
	 */
	private String password;

	public static UserDTOBuilder withAllDummy() {
		return new UserDTOBuilder().setName("John Doe").setUsername("johndoe").setEmail("john@example.com")
				.setPassword("1234");
	}

	public UserDTO build() {
		UserDTO userDTO = new UserDTO();
		userDTO.setName(name);
		userDTO.setUsername(username);
		userDTO.setEmail(email);
		userDTO.setPassword(password);
		return userDTO;
	}

	public UserDTO build2() {
		return new UserDTO(name, username, email, password);
	}

	// Métodos setters para el builder

	/**
	 * Set the value of the property email
	 *
	 * @param email the email to set
	 */
	public UserDTOBuilder setEmail(String email) {
		this.email = email;
		return this;
	}

	/**
	 * Set the value of the property name
	 *
	 * @param name the name to set
	 */
	public UserDTOBuilder setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Set the value of the property password
	 *
	 * @param password the password to set
	 */
	public UserDTOBuilder setPassword(String password) {
		this.password = password;
		return this;
	}

	/**
	 * Set the value of the property username
	 *
	 * @param username the username to set
	 */
	public UserDTOBuilder setUsername(String username) {
		this.username = username;
		return this;
	}
}
