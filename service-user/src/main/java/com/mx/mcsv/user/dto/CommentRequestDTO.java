package com.mx.mcsv.user.dto;

public class CommentRequestDTO {

	private Long userId;

	private Long blogId;

	private String content;

	/**
	 * 
	 */
	public CommentRequestDTO() {
	}

	/**
	 * @param userId
	 * @param blogId
	 * @param content
	 */
	public CommentRequestDTO(Long userId, Long blogId, String content) {
		this.userId = userId;
		this.blogId = blogId;
		this.content = content;
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
	 * set the value of the property userId
	 *
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
