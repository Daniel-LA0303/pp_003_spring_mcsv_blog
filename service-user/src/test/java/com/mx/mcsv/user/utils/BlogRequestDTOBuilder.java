package com.mx.mcsv.user.utils;

import com.mx.mcsv.user.dto.BlogRequestDTO;

public class BlogRequestDTOBuilder {

	/**
	 * Title
	 */
	private String title;

	/**
	 * Description
	 */
	private String description;

	/**
	 * Content
	 */
	private String content;

	/**
	 * UserId
	 */
	private Long userId;

	/**
	 * Status
	 */
	private String status;

	/**
	 * Método para construir un objeto `BlogRequestDTO` con datos de ejemplo (dummy)
	 * 
	 * @return BlogRequestDTOBuilder con datos de ejemplo
	 */
	public static BlogRequestDTOBuilder withAllDummy() {
		return new BlogRequestDTOBuilder().setTitle("Example Title").setDescription("This is a dummy description.")
				.setContent("Sample content for the blog post.").setUserId(1L).setStatus("Draft");
	}

	/**
	 * Método para construir un objeto `BlogRequestDTO`
	 * 
	 * @return objeto BlogRequestDTO
	 */
	public BlogRequestDTO build() {
		BlogRequestDTO blogRequestDTO = new BlogRequestDTO();
		blogRequestDTO.setTitle(title);
		blogRequestDTO.setDescription(description);
		blogRequestDTO.setContent(content);
		blogRequestDTO.setUserId(userId);
		blogRequestDTO.setStatus(status);
		return blogRequestDTO;
	}

	/**
	 * Método alternativo de construcción usando un constructor con parámetros
	 * 
	 * @return objeto BlogRequestDTO
	 */
	public BlogRequestDTO build2() {
		return new BlogRequestDTO(title, description, content, userId, status);
	}

	// Métodos setters para el builder

	/**
	 * Set the value of the property content
	 *
	 * @param content the content to set
	 */
	public BlogRequestDTOBuilder setContent(String content) {
		this.content = content;
		return this;
	}

	/**
	 * Set the value of the property description
	 *
	 * @param description the description to set
	 */
	public BlogRequestDTOBuilder setDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Set the value of the property status
	 *
	 * @param status the status to set
	 */
	public BlogRequestDTOBuilder setStatus(String status) {
		this.status = status;
		return this;
	}

	/**
	 * Set the value of the property title
	 *
	 * @param title the title to set
	 */
	public BlogRequestDTOBuilder setTitle(String title) {
		this.title = title;
		return this;
	}

	/**
	 * Set the value of the property userId
	 *
	 * @param userId the userId to set
	 */
	public BlogRequestDTOBuilder setUserId(Long userId) {
		this.userId = userId;
		return this;
	}
}
