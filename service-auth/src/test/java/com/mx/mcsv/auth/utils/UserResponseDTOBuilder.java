package com.mx.mcsv.auth.utils;

import com.mx.mcsv.auth.dto.UserResponseDTO;

public class UserResponseDTOBuilder {

	private Long id;
	private String name;
	private String username;
	private String email;
	private String password;

	public static UserResponseDTOBuilder withAllDummy() {
		return new UserResponseDTOBuilder().setId(1L).setName("John Doe").setUsername("johndoe")
				.setEmail("john@example.com").setPassword("hashed_password_1");
	}

	public UserResponseDTO build() {
		UserResponseDTO userResponseDTO = new UserResponseDTO();
		userResponseDTO.setId(this.id);
		userResponseDTO.setName(this.name);
		userResponseDTO.setUsername(this.username);
		userResponseDTO.setEmail(this.email);
		userResponseDTO.setPassword(this.password);
		return userResponseDTO;
	}

	public UserResponseDTOBuilder setEmail(String email) {
		this.email = email;
		return this;
	}

	public UserResponseDTOBuilder setId(Long id) {
		this.id = id;
		return this;
	}

	public UserResponseDTOBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public UserResponseDTOBuilder setPassword(String password) {
		this.password = password;
		return this;
	}

	public UserResponseDTOBuilder setUsername(String username) {
		this.username = username;
		return this;
	}
}
