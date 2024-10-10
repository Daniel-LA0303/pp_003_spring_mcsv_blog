package com.mx.mcsv.user.dto;

public class BlogRequestDTO {

	private String title;

	private String description;

	private String content;

	private Long userId;

	private String status;

	/**
	 * 
	 */
	public BlogRequestDTO() {
	}

	/**
	 * @param title
	 * @param description
	 * @param content
	 * @param userId
	 * @param status
	 */
	public BlogRequestDTO(String title, String description, String content, Long userId, String status) {
		this.title = title;
		this.description = description;
		this.content = content;
		this.userId = userId;
		this.status = status;
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
	 * return the value of the property description
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * return the value of the property status
	 *
	 * @return the status
	 */
	public String getStatus() {
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
	 * set the value of the property description
	 *
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * set the value of the property status
	 *
	 * @param status the status to set
	 */
	public void setStatus(String status) {
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
