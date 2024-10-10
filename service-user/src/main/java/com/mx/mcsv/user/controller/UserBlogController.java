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
import com.mx.mcsv.user.dto.BlogRequestDTO;
import com.mx.mcsv.user.exceptions.UserException;
import com.mx.mcsv.user.service.UserService;
import com.mx.mcsv.user.service.impl.UserBlogServiceImpl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/api/users/blog")
public class UserBlogController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserBlogServiceImpl userBlogService;

	@PostMapping("")
	@CircuitBreaker(name = "blogCB", fallbackMethod = "fallBackCreateBlogForUser")
	public ResponseEntity<?> createBlogForUser(@RequestBody BlogRequestDTO blogRequest) throws UserException {

		userService.findById(blogRequest.getUserId());

		ApiResponse<Object, Object> apiResponse = userBlogService.createBlog(blogRequest);

		// HttpStatus status = HttpStatus.valueOf(apiResponse.getStatus());
		return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
	}

	@DeleteMapping("/{id}/{userId}")
	@CircuitBreaker(name = "blogCB", fallbackMethod = "fallBackDeleteBlog")
	public ResponseEntity<?> deteleBlog(@PathVariable Long id, @PathVariable Long userId) throws UserException {

		userService.findById(userId);

		ApiResponse<Object, Object> apiResponse = userBlogService.deteleBlogByUser(id, userId);

		return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
	}

	@GetMapping("/{id}")
	@CircuitBreaker(name = "blogCB", fallbackMethod = "fallBackGetBlogsByUser")
	public ResponseEntity<?> getBlogsByUser(@PathVariable Long id) throws UserException {

		userService.findById(id);

		ApiResponse<Object, Object> apiResponse = userBlogService.getBlogsByUser(id);

		return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));

	}

	private ResponseEntity<?> fallBackCreateBlogForUser(BlogRequestDTO blogRequest, RuntimeException e) {
		return new ResponseEntity<>("Could not create the blog at this time, try later.", HttpStatus.OK);
	}

	private ResponseEntity<?> fallBackDeleteBlog(Long id, Long userId, RuntimeException e) {
		return new ResponseEntity<>("The blog could not be deleted at this time.", HttpStatus.OK);
	}

	// Fallback method for getting blogs by user
	private ResponseEntity<?> fallBackGetBlogsByUser(Long id, RuntimeException e) {
		return new ResponseEntity<>("Unable to obtain user blogs at this time.", HttpStatus.OK);
	}

}
