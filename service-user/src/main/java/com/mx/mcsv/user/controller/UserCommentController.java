package com.mx.mcsv.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mx.mcsv.user.dto.ApiResponse;
import com.mx.mcsv.user.dto.CommentRequestDTO;
import com.mx.mcsv.user.exceptions.UserException;
import com.mx.mcsv.user.service.UserService;
import com.mx.mcsv.user.service.impl.UserCommentServiceImpl;

@RestController
@RequestMapping("/api/users/comment")
public class UserCommentController {

	@Autowired
	private UserCommentServiceImpl userCommentService;

	@Autowired
	private UserService userService;

	@PostMapping
	public ResponseEntity<?> createComment(@RequestBody CommentRequestDTO commentRequestDTO) throws UserException {
		userService.findById(commentRequestDTO.getUserId());

		ApiResponse<Object, Object> apiResponse = userCommentService.createComment(commentRequestDTO);
		return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));

	}

	@DeleteMapping("/delete-comment/{id}/{userId}/{blogId}")
	public ResponseEntity<?> deteleteComment(@PathVariable Long id, @PathVariable Long userId,
			@PathVariable Long blogId) throws UserException {

		userService.findById(userId);

		ApiResponse<Object, Object> apiResponse = userCommentService.deleteComment(id, userId, blogId);

		return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));

	}

	@GetMapping("/by-blog/{id}")
	public ResponseEntity<?> getCommentsByBlog(@PathVariable Long id) throws UserException {
		ApiResponse<Object, Object> apiResponse = userCommentService.getCommentsByBlog(id);

		return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
	}

	@GetMapping("/by-user/{id}")
	public ResponseEntity<?> getCommentsByUser(@PathVariable Long id) throws UserException {
		userService.findById(id);

		ApiResponse<Object, Object> apiResponse = userCommentService.getCommentsByUser(id);

		return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
	}

}
