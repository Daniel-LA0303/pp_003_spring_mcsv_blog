package com.mx.mcsv.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "users_blog")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "The field name must not be blank")
	@Column(name = "name")
	private String name;

	@NotBlank(message = "The field username must not be blank")
	@Column(name = "username")
	private String username;

	@NotBlank(message = "The field email must not be blank")
	@Column(name = "email")
	private String email;

	@NotEmpty(message = "The field password must not be null")
	@Column(name = "password")
	private String password;

	/**
	 * 
	 */
	public User() {
	}

	/**
	 * @param id
	 * @param name
	 * @param username
	 * @param email
	 * @param password
	 */
	public User(Long id, @NotBlank String name, @NotBlank String username, @NotBlank String email,
			@NotEmpty String password) {
		this.id = id;
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
	 * return the value of the property id
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
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
	 * set the value of the property id
	 *
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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
