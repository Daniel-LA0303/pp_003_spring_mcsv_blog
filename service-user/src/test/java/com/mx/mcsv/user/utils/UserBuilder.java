package com.mx.mcsv.user.utils;

import com.mx.mcsv.user.entity.User;

public class UserBuilder {

	/**
	 * Id
	 */
	private Long id;

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

	/**
	 * Método para construir un objeto `User` con datos de ejemplo (dummy)
	 * 
	 * @return UserBuilder con datos de ejemplo
	 */
	public static UserBuilder withAllDummy() {
		return new UserBuilder().setId(1L).setName("John Doe").setUsername("johndoe").setEmail("john@example.com")
				.setPassword("hashed_password_1");
	}

	/**
	 * Método para construir un objeto `User`
	 * 
	 * @return objeto User
	 */
	public User build() {
		User user = new User();
		user.setId(id);
		user.setName(name);
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(password);
		return user;
	}

	/**
	 * Método alternativo de construcción usando un constructor con parámetros
	 * 
	 * @return objeto User
	 */
	public User build2() {
		return new User(id, name, username, email, password);
	}

	// Métodos setters para el builder

	/**
	 * Set the value of the property email
	 *
	 * @param email the email to set
	 */
	public UserBuilder setEmail(String email) {
		this.email = email;
		return this;
	}

	/**
	 * Set the value of the property id
	 *
	 * @param id the id to set
	 */
	public UserBuilder setId(Long id) {
		this.id = id;
		return this;
	}

	/**
	 * Set the value of the property name
	 *
	 * @param name the name to set
	 */
	public UserBuilder setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Set the value of the property password
	 *
	 * @param password the password to set
	 */
	public UserBuilder setPassword(String password) {
		this.password = password;
		return this;
	}

	/**
	 * Set the value of the property username
	 *
	 * @param username the username to set
	 */
	public UserBuilder setUsername(String username) {
		this.username = username;
		return this;
	}
}
