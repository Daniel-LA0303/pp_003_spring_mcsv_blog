package com.mx.mcsv.blog.controller;

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

import com.mx.mcsv.blog.dto.ApiResponse;
import com.mx.mcsv.blog.dto.BlogDTO;
import com.mx.mcsv.blog.entity.Blog;
import com.mx.mcsv.blog.exception.BlogException;
import com.mx.mcsv.blog.service.BlogService;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

	@Autowired
	private BlogService blogService;

	@DeleteMapping("/{id}/{userId}")
	public ResponseEntity<?> deleteBlog(@PathVariable Long id, @PathVariable Long userId) throws BlogException {
		blogService.deleteBlog(id, userId);

		ApiResponse<String, String> apiResponse = new ApiResponse<>(200, "Blog deleted", null);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<?> getAllBlogs() {
		List<BlogDTO> blogs = blogService.findAll();

		ApiResponse<List<BlogDTO>, String> apiResponse = new ApiResponse<>(200, blogs, null);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getBlog(@PathVariable Long id) throws BlogException {
		BlogDTO blogDTO = blogService.findById(id);
		ApiResponse<BlogDTO, String> apiResponse = new ApiResponse<>(200, blogDTO, null);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@GetMapping("/get-blogs-by-id/{id}")
	public ResponseEntity<?> getBlogsByUser(@PathVariable Long id) throws BlogException {

		List<BlogDTO> blogsResponse = blogService.findByUserId(id);
		ApiResponse<List<BlogDTO>, String> apiResponse = new ApiResponse<>(200, blogsResponse, null);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);

	}

	@PostMapping
	public ResponseEntity<?> saveBlog(@Valid @RequestBody Blog blog, BindingResult result) throws BlogException {

		if (result.hasErrors()) {
			return validation(result);
		}

		BlogDTO blogDTO = blogService.save(blog);
		ApiResponse<BlogDTO, String> apiResponse = new ApiResponse<>(201, blogDTO, null);
		return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateBlog(@PathVariable Long id, @Valid @RequestBody Blog blog, BindingResult result)
			throws BlogException {

		if (result.hasErrors()) {
			return validation(result);
		}

		BlogDTO updatedBlogDTO = blogService.update(id, blog);

		ApiResponse<BlogDTO, String> apiResponse = new ApiResponse<>(200, updatedBlogDTO, null);
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
