package com.mx.mcsv.blog.dto;

import java.time.LocalDateTime;

import com.mx.mcsv.blog.enums.BlogStatus;

public class BlogDTO {

	private Long id;

	private String title;

	private String description;

	private String content;

	private Long userId;

	private BlogStatus status;

	private LocalDateTime createdAt;

	/**
	 * 
	 */
	public BlogDTO() {
	}

	/**
	 * @param id
	 * @param title
	 * @param description
	 * @param content
	 * @param userId
	 * @param status
	 * @param createdAt
	 */
	public BlogDTO(Long id, String title, String description, String content, Long userId, BlogStatus status,
			LocalDateTime createdAt) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.content = content;
		this.userId = userId;
		this.status = status;
		this.createdAt = createdAt;
	}

	/**
	 * return the value of the property content
	 *
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * return the value of the property createdAt
	 *
	 * @return the createdAt
	 */
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	/**
	 * return the value of the property description
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
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
	 * return the value of the property status
	 *
	 * @return the status
	 */
	public BlogStatus getStatus() {
		return status;
	}

	/**
	 * return the value of the property title
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * return the value of the property userId
	 *
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * set the value of the property content
	 *
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * set the value of the property createdAt
	 *
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * set the value of the property description
	 *
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * set the value of the property status
	 *
	 * @param status the status to set
	 */
	public void setStatus(BlogStatus status) {
		this.status = status;
	}

	/**
	 * set the value of the property title
	 *
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * set the value of the property userId
	 *
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
