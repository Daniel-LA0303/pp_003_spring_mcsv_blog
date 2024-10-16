package com.mx.mcsv.user.utils;

import com.mx.mcsv.user.dto.CommentRequestDTO;

public class CommentRequestDTOBuilder {

	/**
	 * UserId
	 */
	private Long userId;

	/**
	 * BlogId
	 */
	private Long blogId;

	/**
	 * Content
	 */
	private String content;

	public static CommentRequestDTOBuilder withAllDummy() {
		return new CommentRequestDTOBuilder().setUserId(1L).setBlogId(1L)
				.setContent("This is a sample comment for the blog post.");
	}

	public CommentRequestDTO build() {
		CommentRequestDTO commentRequestDTO = new CommentRequestDTO();
		commentRequestDTO.setUserId(userId);
		commentRequestDTO.setBlogId(blogId);
		commentRequestDTO.setContent(content);
		return commentRequestDTO;
	}

	public CommentRequestDTO build2() {
		return new CommentRequestDTO(userId, blogId, content);
	}

	/**
	 * Set the value of the property blogId
	 *
	 * @param blogId the blogId to set
	 */
	public CommentRequestDTOBuilder setBlogId(Long blogId) {
		this.blogId = blogId;
		return this;
	}

	/**
	 * Set the value of the property content
	 *
	 * @param content the content to set
	 */
	public CommentRequestDTOBuilder setContent(String content) {
		this.content = content;
		return this;
	}

	/**
	 * Set the value of the property userId
	 *
	 * @param userId the userId to set
	 */
	public CommentRequestDTOBuilder setUserId(Long userId) {
		this.userId = userId;
		return this;
	}
}