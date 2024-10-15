package com.mx.mcsv.blog.utils;

import java.time.LocalDateTime;

import com.mx.mcsv.blog.entity.Blog;
import com.mx.mcsv.blog.enums.BlogStatus;

public class BlogBuilder {

	/**
	 * Id
	 */
	private Long id;

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
	 * User Id
	 */
	private Long userId;

	/**
	 * Status
	 */
	private BlogStatus status;

	/**
	 * Created at
	 */
	private LocalDateTime createdAt;

	/**
	 * Updated at
	 */
	private LocalDateTime updatedAt;

	public static BlogBuilder withAllDummy() {
		return new BlogBuilder().setId(1L).setTitle("The Evolution of Tech")
				.setDescription("A detailed exploration of technological advances over the last decade.")
				.setContent("This blog post delves into the major innovations that have transformed industries.")
				.setUserId(1L).setStatus(BlogStatus.PUBLISHED).setCreatedAt(LocalDateTime.parse("2024-10-11T10:45:00"))
				.setUpdatedAt(LocalDateTime.parse("2024-10-11T10:45:00"));
	}

	public Blog build() {
		Blog blog = new Blog();
		blog.setId(id);
		blog.setTitle(title);
		blog.setDescription(description);
		blog.setContent(content);
		blog.setUserId(userId);
		blog.setStatus(status);
		blog.setCreatedAt(createdAt);
		blog.setUpdatedAt(updatedAt);
		return blog;
	}

	public Blog build2() {
		return new Blog(id, title, description, content, userId, status, createdAt, updatedAt);
	}

	/**
	 * Set the value of the property content
	 *
	 * @param content the content to set
	 */
	public BlogBuilder setContent(String content) {
		this.content = content;
		return this;
	}

	/**
	 * Set the value of the property createdAt
	 *
	 * @param createdAt the createdAt to set
	 */
	public BlogBuilder setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	/**
	 * Set the value of the property description
	 *
	 * @param description the description to set
	 */
	public BlogBuilder setDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Set the value of the property id
	 *
	 * @param id the id to set
	 */
	public BlogBuilder setId(Long id) {
		this.id = id;
		return this;
	}

	/**
	 * Set the value of the property status
	 *
	 * @param status the status to set
	 */
	public BlogBuilder setStatus(BlogStatus status) {
		this.status = status;
		return this;
	}

	/**
	 * Set the value of the property title
	 *
	 * @param title the title to set
	 */
	public BlogBuilder setTitle(String title) {
		this.title = title;
		return this;
	}

	/**
	 * Set the value of the property updatedAt
	 *
	 * @param updatedAt the updatedAt to set
	 */
	public BlogBuilder setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
		return this;
	}

	/**
	 * Set the value of the property userId
	 *
	 * @param userId the userId to set
	 */
	public BlogBuilder setUserId(Long userId) {
		this.userId = userId;
		return this;
	}
}
