package com.mx.mcsv.user.config.resttemplateconfig;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mx.mcsv.user.dto.ApiResponse;
import com.mx.mcsv.user.dto.CommentRequestDTO;

@FeignClient(name = "service-comments")
public interface CommentOpenFeign {

	// *****PRIMERO REVISAR SI FUNCIONA
	@DeleteMapping("/api/comments/{id}/{userId}")
	public ResponseEntity<ApiResponse<?, ?>> deleteComment(@PathVariable Long id, @PathVariable Long userId);

	@GetMapping("/api/comments/by-blog/{id}")
	public ResponseEntity<ApiResponse<?, ?>> getCommentsByBlog(@PathVariable Long id);

	@GetMapping("/api/comments/by-user/{id}")
	public ResponseEntity<ApiResponse<?, ?>> getCommentsByUser(@PathVariable Long id);

	@PostMapping("/api/comments")
	public ResponseEntity<ApiResponse<?, ?>> saveComment(@RequestBody CommentRequestDTO comment);

}
