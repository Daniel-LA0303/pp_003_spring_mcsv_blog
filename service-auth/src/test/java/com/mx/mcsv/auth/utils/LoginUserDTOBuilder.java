package com.mx.mcsv.auth.utils;

import com.mx.mcsv.auth.dto.LoginUserDTO;

public class LoginUserDTOBuilder {

	/**
	 * Email
	 */
	private String email;

	/**
	 * Password
	 */
	private String password;

	public static LoginUserDTOBuilder withAllDummy() {
		return new LoginUserDTOBuilder().setEmail("john@example.com").setPassword("hashed_password_1");
	}

	public LoginUserDTO build() {
		LoginUserDTO loginUserDTO = new LoginUserDTO();
		loginUserDTO.setEmail(email);
		loginUserDTO.setPassword(password);
		return loginUserDTO;
	}

	public LoginUserDTO build2() {
		return new LoginUserDTO(email, password);
	}

	/**
	 * Set the value of the property email
	 *
	 * @param email the email to set
	 */
	public LoginUserDTOBuilder setEmail(String email) {
		this.email = email;
		return this;
	}

	/**
	 * Set the value of the property password
	 *
	 * @param password the password to set
	 */
	public LoginUserDTOBuilder setPassword(String password) {
		this.password = password;
		return this;
	}
}
