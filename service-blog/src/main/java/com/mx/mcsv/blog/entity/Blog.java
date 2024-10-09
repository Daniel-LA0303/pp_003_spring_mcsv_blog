package com.mx.mcsv.blog.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.mx.mcsv.blog.enums.BlogStatus;

@Entity
@Table(name = "content_blog")
public class Blog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(name = "title", unique = true)
	private String title;

	@NotBlank
	@Column(name = "description")
	private String description;

	@NotBlank
	@Column(name = "content")
	private String content;

	@NotNull
	@Column(name = "user_id")
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private BlogStatus status;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	/**
	 * 
	 */
	public Blog() {
	}

	/**
	 * @param id
	 * @param title
	 * @param description
	 * @param content
	 * @param userId
	 * @param status
	 * @param createdAt
	 * @param updatedAt
	 */
	public Blog(Long id, @NotBlank String title, @NotBlank String description, @NotBlank String content,
			@NotBlank Long userId, @NotBlank BlogStatus status, @NotBlank LocalDateTime createdAt,
			@NotBlank LocalDateTime updatedAt) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.content = content;
		this.userId = userId;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
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
	 * return the value of the property updatedAt
	 *
	 * @return the updatedAt
	 */
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
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
	 * set the value of the property updatedAt
	 *
	 * @param updatedAt the updatedAt to set
	 */
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
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
