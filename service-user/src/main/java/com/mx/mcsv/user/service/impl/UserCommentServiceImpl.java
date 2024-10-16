package com.mx.mcsv.user.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mx.mcsv.user.config.resttemplateconfig.BlogOpenFeign;
import com.mx.mcsv.user.config.resttemplateconfig.CommentOpenFeign;
import com.mx.mcsv.user.dto.ApiResponse;
import com.mx.mcsv.user.dto.CommentRequestDTO;

import feign.FeignException;

@Service
public class UserCommentServiceImpl {

	@Autowired
	private CommentOpenFeign commentOpenFeign;

	@Autowired
	private BlogOpenFeign blogOpenFeign;

	public <T> ApiResponse<T, Object> createComment(CommentRequestDTO commentRequestDTO) {
		try {
			ResponseEntity<?> responseBlog = blogOpenFeign.getBlog(commentRequestDTO.getBlogId());

			ResponseEntity<?> responseComment = commentOpenFeign.saveComment(commentRequestDTO);

			System.out.println("*****");
			System.out.println("comment");

			ApiResponse<T, Object> res = (ApiResponse<T, Object>) responseComment.getBody();

			System.out.println(responseComment.getBody());
			System.out.println("error: " + res.geterror());
			System.out.println("data: " + res.getData());
			System.out.println("status: " + res.getStatus());
			System.out.println("times: " + res.getTimeStamp());

			return res;

			// return convertToApiResponse((Map<String, Object>) responseComment.getBody());

		} catch (FeignException e) {
			String errorMessage = e.contentUTF8();
			Object errorDetails = extractError(errorMessage);
			return new ApiResponse<>(e.status(), null, errorDetails);
		}
	}

	public <T> ApiResponse<T, Object> deleteComment(Long id, Long userId, Long blogId) {
		try {
			ResponseEntity<?> responseBlog = blogOpenFeign.getBlog(blogId);

			ResponseEntity<?> response = commentOpenFeign.deleteComment(id, userId);

			ApiResponse<T, Object> res = (ApiResponse<T, Object>) response.getBody();

			return res;
			// return convertToApiResponse((Map<String, Object>) response.getBody());
		} catch (FeignException e) {
			String errorMessage = e.contentUTF8();

			Object errorDetails = extractError(errorMessage);

			return new ApiResponse<>(e.status(), null, errorDetails);
		}

	}

	public <T> ApiResponse<T, Object> getCommentsByBlog(Long id) {

		try {
			ResponseEntity<?> responseBlog = blogOpenFeign.getBlog(id);

			ResponseEntity<?> response = commentOpenFeign.getCommentsByBlog(id);

			ApiResponse<T, Object> res = (ApiResponse<T, Object>) response.getBody();

			return res;
			// return convertToApiResponse((Map<String, Object>) response.getBody());
		} catch (FeignException e) {
			String errorMessage = e.contentUTF8();

			Object errorDetails = extractError(errorMessage);

			return new ApiResponse<>(e.status(), null, errorDetails);
		}

	}

	public <T> ApiResponse<T, Object> getCommentsByUser(Long id) {

		try {
			ResponseEntity<?> response = commentOpenFeign.getCommentsByUser(id);
			ApiResponse<T, Object> res = (ApiResponse<T, Object>) response.getBody();

			return res;
			// return convertToApiResponse((Map<String, Object>) response.getBody());
		} catch (FeignException e) {
			String errorMessage = e.contentUTF8();

			Object errorDetails = extractError(errorMessage);

			return new ApiResponse<>(e.status(), null, errorDetails);
		}

	}

	private Object extractError(String errorResponse) {
		try {
			String cleanedErrorResponse = errorResponse.substring(errorResponse.indexOf("{"),
					errorResponse.lastIndexOf("}") + 1);

			Map<String, Object> errorMap = new HashMap<>();
			Pattern pattern = Pattern.compile("\"error\":\\{(.*?)\\}");
			Matcher matcher = pattern.matcher(cleanedErrorResponse);

			if (matcher.find()) {
				String errorBody = matcher.group(1);
				String jsonError = "{" + errorBody + "}";

				errorMap = new ObjectMapper().readValue(jsonError, Map.class);
			}

			if (!errorMap.isEmpty()) {
				return errorMap;
			} else {
				return Map.of("error", "No error information found");
			}
		} catch (JsonMappingException e) {
			return Map.of("error", "Failed to extract error details");
		} catch (JsonProcessingException e) {
			return Map.of("error", "Failed to extract error details");
		}
	}

}
