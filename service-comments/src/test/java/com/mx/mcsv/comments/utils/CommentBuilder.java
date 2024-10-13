package com.mx.mcsv.comments.utils;

import java.time.LocalDateTime;

import com.mx.mcsv.comments.entity.Comment;

public class CommentBuilder {

	/**
	 * Id
	 */
	private Long id;

	/**
	 * user id
	 */
	private Long userId;

	/**
	 * blog id
	 */
	private Long blogId;

	/**
	 * content
	 */
	private String content;

	/**
	 * created at
	 */
	private LocalDateTime createdAt;

	/**
	 * updated at
	 */
	private LocalDateTime updatedAt;

	public static CommentBuilder withAllDymmuy() {
		return new CommentBuilder().setId(1L).setBlogId(5L).setContent("Interesting perspective, thanks!")
				.setCreatedAt(LocalDateTime.parse("2024-10-11T10:45:00"))
				.setUpdatedAt(LocalDateTime.parse("2024-10-11T10:45:00")).setUserId(1L);
	}

	public Comment build() {
		Comment comment = new Comment();
		comment.setBlogId(blogId);
		comment.setContent(content);
		comment.setCreatedAt(createdAt);
		comment.setId(id);
		comment.setUpdatedAt(updatedAt);
		comment.setUserId(userId);
		return comment;
	}

	public Comment build2() {
		Comment comment = new Comment(id, userId, blogId, content, createdAt, updatedAt);
		return comment;
	}

	/**
	 * set the value of the property blogId
	 *
	 * @param blogId the blogId to set
	 */
	public CommentBuilder setBlogId(Long blogId) {
		this.blogId = blogId;
		return this;
	}

	/**
	 * set the value of the property content
	 *
	 * @param content the content to set
	 */
	public CommentBuilder setContent(String content) {
		this.content = content;
		return this;
	}

	/**
	 * set the value of the property createdAt
	 *
	 * @param createdAt the createdAt to set
	 */
	public CommentBuilder setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	/**
	 * set the value of the property id
	 *
	 * @param id the id to set
	 */
	public CommentBuilder setId(Long id) {
		this.id = id;
		return this;
	}

	/**
	 * set the value of the property updatedAt
	 *
	 * @param updatedAt the updatedAt to set
	 */
	public CommentBuilder setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
		return this;
	}

	/**
	 * set the value of the property userId
	 *
	 * @param userId the userId to set
	 */
	public CommentBuilder setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

}
