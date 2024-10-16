package com.mx.mcsv.user.services.communication.comment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mx.mcsv.user.controller.UserCommentController;
import com.mx.mcsv.user.dto.ApiResponse;
import com.mx.mcsv.user.dto.BlogRequestDTO;
import com.mx.mcsv.user.dto.CommentRequestDTO;
import com.mx.mcsv.user.dto.UserDTO;
import com.mx.mcsv.user.service.UserService;
import com.mx.mcsv.user.service.impl.UserCommentServiceImpl;
import com.mx.mcsv.user.utils.CommentRequestDTOBuilder;

@ExtendWith(MockitoExtension.class)
public class UserCommentControllarSuccessTest {

	@InjectMocks
	private UserCommentController userCommentController;

	@Mock
	private UserService userService;

	@Mock
	private UserCommentServiceImpl userCommentService;

	CommentRequestDTO commentRequestDTO;

	@Test
	public void testCreateCommentForUserSuccess() throws Exception {

		ApiResponse<Object, Object> apiResponse = new ApiResponse<>(HttpStatus.CREATED.value(), commentRequestDTO,
				null);

		Mockito.when(userService.findById(commentRequestDTO.getUserId())).thenReturn(new UserDTO());
		Mockito.when(userCommentService.createComment(commentRequestDTO)).thenReturn(apiResponse);

		ResponseEntity<ApiResponse<CommentRequestDTO, String>> responseEntity = (ResponseEntity<ApiResponse<CommentRequestDTO, String>>) userCommentController
				.createComment(commentRequestDTO);

		System.out.println("*******" + responseEntity.getStatusCode());
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(apiResponse, responseEntity.getBody());
		assertEquals(null, apiResponse.geterror());

		CommentRequestDTO commentResponse = responseEntity.getBody().getData();
		assertEquals(commentRequestDTO.getContent(), commentResponse.getContent());
		assertEquals(commentRequestDTO.getBlogId(), commentResponse.getBlogId());
		assertEquals(commentRequestDTO.getUserId(), commentResponse.getUserId());

		Mockito.verify(userService).findById(commentRequestDTO.getUserId());
		Mockito.verify(userCommentService).createComment(commentResponse);
	}

	@Test
	public void testDeleteCommentForUserSuccess() throws Exception {

		ApiResponse<Object, Object> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Comment deleted", null);

		Mockito.when(userService.findById(1L)).thenReturn(new UserDTO());
		Mockito.when(userCommentService.deleteComment(1L, 1L, 1L)).thenReturn(apiResponse);

		ResponseEntity<ApiResponse<String, String>> responseEntity = (ResponseEntity<ApiResponse<String, String>>) userCommentController
				.deteleteComment(1L, 1L, 1L);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(apiResponse, responseEntity.getBody());
		assertEquals("Comment deleted", responseEntity.getBody().getData());
		assertEquals(null, responseEntity.getBody().geterror());

		Mockito.verify(userService).findById(1L);
		Mockito.verify(userCommentService).deleteComment(1L, 1L, 1L);
	}

	@Test
	public void testGetCommentsByBlogSuccess() throws Exception {
		CommentRequestDTO commentRequestDTO2 = CommentRequestDTOBuilder.withAllDummy().setBlogId(1L)
				.setContent("Content comment").setUserId(1L).build2();

		List<CommentRequestDTO> comments = new ArrayList<>();
		comments.add(commentRequestDTO);
		comments.add(commentRequestDTO2);

		ApiResponse<Object, Object> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), comments, null);
		apiResponse.setStatus(200);

		Mockito.when(userCommentService.getCommentsByBlog(1L)).thenReturn(apiResponse);

		ResponseEntity<ApiResponse<List<BlogRequestDTO>, String>> responseEntity = (ResponseEntity<ApiResponse<List<BlogRequestDTO>, String>>) userCommentController
				.getCommentsByBlog(1L);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(apiResponse, responseEntity.getBody());
		assertEquals(comments, responseEntity.getBody().getData());
		assertEquals(2, responseEntity.getBody().getData().size());
		assertEquals(null, responseEntity.getBody().geterror());

		Mockito.verify(userCommentService).getCommentsByBlog(1L);
	}

	@Test
	public void testGetCommentsByUserSuccess() throws Exception {
		CommentRequestDTO commentRequestDTO2 = CommentRequestDTOBuilder.withAllDummy().setBlogId(1L)
				.setContent("Content comment").setUserId(1L).build2();

		List<CommentRequestDTO> comments = new ArrayList<>();
		comments.add(commentRequestDTO);
		comments.add(commentRequestDTO2);

		ApiResponse<Object, Object> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), comments, null);
		apiResponse.setStatus(200);

		Mockito.when(userService.findById(1L)).thenReturn(new UserDTO());
		Mockito.when(userCommentService.getCommentsByUser(1L)).thenReturn(apiResponse);

		ResponseEntity<ApiResponse<List<BlogRequestDTO>, String>> responseEntity = (ResponseEntity<ApiResponse<List<BlogRequestDTO>, String>>) userCommentController
				.getCommentsByUser(1L);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(apiResponse, responseEntity.getBody());
		assertEquals(comments, responseEntity.getBody().getData());
		assertEquals(2, responseEntity.getBody().getData().size());
		assertEquals(null, responseEntity.getBody().geterror());

		Mockito.verify(userService).findById(1L);
		Mockito.verify(userCommentService).getCommentsByUser(1L);
	}

	@BeforeEach
	void setUp() {

		commentRequestDTO = CommentRequestDTOBuilder.withAllDummy().build();

	}

}
