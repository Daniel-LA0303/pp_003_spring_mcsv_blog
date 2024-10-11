package com.mx.mcsv.user.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mx.mcsv.user.dto.ApiResponse;
import com.mx.mcsv.user.dto.BlogRequestDTO;

@Service
public class UserBlogServiceImpl {

	@Autowired
	private RestTemplate restTemplate;

	private final ObjectMapper objectMapper = new ObjectMapper();

	public <T> ApiResponse<T, Object> createBlog(BlogRequestDTO blog) {
		try {

			ResponseEntity<ApiResponse<T, Object>> response = restTemplate.postForEntity(
					"http://service-blog/api/blogs", blog,
					(Class<ApiResponse<T, Object>>) (Class<?>) ApiResponse.class);

			return response.getBody();

		} catch (HttpClientErrorException e) {
			String errorMessage = e.getResponseBodyAsString();
			Object errorDetails = extractError(errorMessage);

			return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), null, errorDetails);
		}
	}

	public <T> ApiResponse<T, Object> deteleBlogByUser(Long id, Long userId) {
		try {

			ResponseEntity<ApiResponse<T, Object>> response = restTemplate.exchange(
					"http://service-blog/api/blogs/{id}/{userId}", HttpMethod.DELETE, null,
					(Class<ApiResponse<T, Object>>) (Class<?>) ApiResponse.class, id, userId);

			return response.getBody();

		} catch (HttpClientErrorException e) {
			String errorMessage = e.getResponseBodyAsString();
			Object errorDetails = extractError(errorMessage);

			if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
				return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), null, errorDetails);
			} else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
				return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), null, errorDetails);
			} else {
				return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), null, errorDetails);
			}
		}
	}

	public <T> ApiResponse<T, Object> getBlogsByUser(Long id) {
		try {

			ResponseEntity<ApiResponse<T, Object>> response = restTemplate.getForEntity(
					"http://service-blog/api/blogs/get-blogs-by-id/{id}",
					(Class<ApiResponse<T, Object>>) (Class<?>) ApiResponse.class, id);

			return response.getBody();

		} catch (Exception e) {
			String errorMessage = e.getMessage();

			Object errorDetails = extractError(errorMessage);

			return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), null, errorDetails);
		}
	}

	private Object extractError(String errorResponse) {
		try {

			String cleanedErrorResponse = errorResponse.substring(errorResponse.indexOf("{"),
					errorResponse.lastIndexOf("}") + 1);

			Map<String, Object> errorMap = objectMapper.readValue(cleanedErrorResponse, Map.class);

			if (errorMap.containsKey("error")) {
				return errorMap.get("error");
			} else {
				return Map.of("error", "No error information found");
			}
		} catch (JsonProcessingException e) {
			return Map.of("error", "Failed to parse error response");
		}
	}

}
