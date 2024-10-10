package com.mx.mcsv.comments.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mx.mcsv.comments.dto.ApiResponse;
import com.mx.mcsv.comments.dto.CommentDTO;
import com.mx.mcsv.comments.entity.Comment;
import com.mx.mcsv.comments.exceptions.CommentException;
import com.mx.mcsv.comments.service.CommentService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

	@Autowired
	private CommentService commentService;

	@DeleteMapping("/{id}/{userId}")
	public ResponseEntity<?> deleteComment(@PathVariable Long id, @PathVariable Long userId) throws CommentException {
		commentService.deleteComment(id, userId);

		ApiResponse<String, String> apiResponse = new ApiResponse<>(200, "Comment deleted", null);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<?> getAllComments() {
		List<CommentDTO> blogs = commentService.findAll();

		ApiResponse<List<CommentDTO>, String> apiResponse = new ApiResponse<>(200, blogs, null);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getComment(@PathVariable Long id) throws CommentException {
		CommentDTO CommentDTO = commentService.findById(id);
		ApiResponse<CommentDTO, String> apiResponse = new ApiResponse<>(200, CommentDTO, null);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@GetMapping("/by-blog/{id}")
	public ResponseEntity<?> getCommentsByBlog(@PathVariable Long id) throws CommentException {

		List<CommentDTO> comments = commentService.getCommentsByBlog(id);

		ApiResponse<List<CommentDTO>, String> apiResponse = new ApiResponse<>(200, comments, null);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@GetMapping("/by-user/{id}")
	public ResponseEntity<?> getCommentsByUser(@PathVariable Long id) throws CommentException {

		List<CommentDTO> comments = commentService.getCommentsByUser(id);

		ApiResponse<List<CommentDTO>, String> apiResponse = new ApiResponse<>(200, comments, null);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> saveComment(@Valid @RequestBody Comment comment, BindingResult result)
			throws CommentException {

		if (result.hasErrors()) {
			return validation(result);
		}

		CommentDTO CommentDTO = commentService.save(comment);
		ApiResponse<CommentDTO, String> apiResponse = new ApiResponse<>(201, CommentDTO, null);
		return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateController(@PathVariable Long id, @Valid @RequestBody Comment comment,
			BindingResult result) throws CommentException {

		if (result.hasErrors()) {
			return validation(result);
		}

		CommentDTO updatedCommentDTO = commentService.update(id, comment);

		ApiResponse<CommentDTO, String> apiResponse = new ApiResponse<>(200, updatedCommentDTO, null);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	private ResponseEntity<ApiResponse<Map<String, String>, Map<String, String>>> validation(BindingResult result) {
		Map<String, String> errorsMap = new HashMap<>();

		result.getFieldErrors().forEach(err -> {
			errorsMap.put(err.getField(), "The field " + err.getField() + " " + err.getDefaultMessage());
		});

		ApiResponse<Map<String, String>, Map<String, String>> apiResponse = new ApiResponse<>(400, null, errorsMap);
		return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
	}

}
