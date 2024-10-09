package com.mx.mcsv.blog.enums;

public enum BlogStatus {
	DRAFT("The blog is in draft status and is not available to readers."),
	PUBLISHED("The blog has been published and is available to readers."),
	ARCHIVED("The blog has been archived and is no longer available to readers.");

	private final String description;

	BlogStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public String getReadableName() {
		return name().charAt(0) + name().substring(1).toLowerCase();
	}
}