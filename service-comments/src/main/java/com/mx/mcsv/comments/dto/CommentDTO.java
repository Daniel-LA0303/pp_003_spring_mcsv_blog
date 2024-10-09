package com.mx.mcsv.comments.dto;

import java.time.LocalDateTime;

public class CommentDTO {

	private Long id;

	private Long userId;

	private Long blogId;

	private String content;

	private LocalDateTime createdAt;

	/**
	 * 
	 */
	public CommentDTO() {
	}

	/**
	 * @param id
	 * @param userId
	 * @param blogId
	 * @param content
	 * @param createdAt
	 */
	public CommentDTO(Long id, Long userId, Long blogId, String content, LocalDateTime createdAt) {
		this.id = id;
		this.userId = userId;
		this.blogId = blogId;
		this.content = content;
		this.createdAt = createdAt;
	}

	/**
	 * return the value of the property blogId
	 *
	 * @return the blogId
	 */
	public Long getBlogId() {
		return blogId;
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
	 * return the value of the property id
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
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
	 * set the value of the property blogId
	 *
	 * @param blogId the blogId to set
	 */
	public void setBlogId(Long blogId) {
		this.blogId = blogId;
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
	 * set the value of the property id
	 *
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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
