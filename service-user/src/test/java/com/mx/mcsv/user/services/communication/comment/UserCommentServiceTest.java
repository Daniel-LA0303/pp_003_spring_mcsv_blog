package com.mx.mcsv.user.services.communication.comment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.mx.mcsv.user.config.resttemplateconfig.BlogOpenFeign;
import com.mx.mcsv.user.config.resttemplateconfig.CommentOpenFeign;
import com.mx.mcsv.user.dto.ApiResponse;
import com.mx.mcsv.user.dto.CommentRequestDTO;
import com.mx.mcsv.user.service.impl.UserCommentServiceImpl;
import com.mx.mcsv.user.utils.CommentRequestDTOBuilder;

import feign.FeignException;
import feign.Request;
import feign.Response;

@SpringBootTest
public class UserCommentServiceTest {

	@InjectMocks
	private UserCommentServiceImpl userCommentService;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private BlogOpenFeign blogOpenFeign;

	@Mock
	private CommentOpenFeign commentOpenFeign;

	CommentRequestDTO commentRequestDTO;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);

		commentRequestDTO = CommentRequestDTOBuilder.withAllDummy().build();
	}

	@Test
	@Order(2)
	void testCreateBlogErrorException() {

		String errorMessage = "{" + "\"status\": 400," + "\"data\": null," + "\"error\": {"
				+ "\"error\": \"Comment Error\"," + "\"message\": \"A error ocurred\","
				+ "\"timeStamp\": \"2024-10-15T17:12:46.8787456\"" + "},"
				+ "\"timeStamp\": \"2024-10-15T17:12:46.8787456\"" + "}";

		FeignException feignException = FeignException.errorStatus("Bad Request",
				Response.builder().status(HttpStatus.BAD_REQUEST.value()).body(errorMessage.getBytes())
						.request(Request.create(Request.HttpMethod.POST, "/api/comments", Map.of(), null, null, null))
						.build());

		Mockito.when(commentOpenFeign.saveComment(commentRequestDTO)).thenThrow(feignException);

		ApiResponse<Object, Object> result = userCommentService.createComment(commentRequestDTO);

		assertNotNull(result);
		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
		assertNotNull(result.geterror());
	}

	@Test
	@Order(1)
	void testCreateCommentSuccess() {

		ApiResponse<Object, Object> apiResponse = new ApiResponse<>(HttpStatus.CREATED.value(), commentRequestDTO,
				null);

		ResponseEntity<ApiResponse<?, ?>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);
		Mockito.when(commentOpenFeign.saveComment(commentRequestDTO)).thenReturn(responseEntity);

		ApiResponse<Object, Object> result = userCommentService.createComment(commentRequestDTO);

		CommentRequestDTO commentResponse = (CommentRequestDTO) result.getData();

		assertNotNull(result);
		assertEquals(201, result.getStatus());
		assertEquals(null, result.geterror());

		assertNotNull(commentResponse);
		assertEquals(commentRequestDTO.getBlogId(), commentResponse.getBlogId());
		assertEquals(commentRequestDTO.getContent(), commentResponse.getContent());
		assertEquals(commentRequestDTO.getUserId(), commentResponse.getUserId());

	}

	@Test
	@Order(4)
	void testDeleteCommentExcepcion() {

		String errorMessage = "{" + "\"status\": 400," + "\"data\": null," + "\"error\": {"
				+ "\"error\": \"Comment Error\"," + "\"message\": \"A error ocurred\","
				+ "\"timeStamp\": \"2024-10-15T17:12:46.8787456\"" + "},"
				+ "\"timeStamp\": \"2024-10-15T17:12:46.8787456\"" + "}";

		FeignException feignException = FeignException.errorStatus("Bad Request",
				Response.builder().status(HttpStatus.BAD_REQUEST.value()).body(errorMessage.getBytes()).request(Request
						.create(Request.HttpMethod.DELETE, "/api/comments/{id}/{userId}", Map.of(), null, null, null))
						.build());

		Mockito.when(commentOpenFeign.deleteComment(1L, 1L)).thenThrow(feignException);

		ApiResponse<Object, Object> result = userCommentService.deleteComment(1L, 1L, 1L);

		assertNotNull(result);
		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
		assertNotNull(result.geterror());

	}

	@Test
	@Order(3)
	void testDeleteCommentSuccess() {

		ApiResponse<Object, Object> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Comment deleted", null);

		ResponseEntity<ApiResponse<?, ?>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);
		Mockito.when(commentOpenFeign.deleteComment(1L, 1L)).thenReturn(responseEntity);

		ApiResponse<Object, Object> result = userCommentService.deleteComment(1L, 1L, 1L);

		assertNotNull(result);
		assertEquals("Comment deleted", result.getData());
		assertEquals(200, result.getStatus());
		assertEquals(null, result.geterror());

	}

	@Test
	@Order(6)
	void testGetCommentsByBlogExcepcion() {

		String errorMessage = "{" + "\"status\": 400," + "\"data\": null," + "\"error\": {"
				+ "\"error\": \"Comment Error\"," + "\"message\": \"A error ocurred\","
				+ "\"timeStamp\": \"2024-10-15T17:12:46.8787456\"" + "},"
				+ "\"timeStamp\": \"2024-10-15T17:12:46.8787456\"" + "}";

		FeignException feignException = FeignException.errorStatus("Bad Request",
				Response.builder().status(HttpStatus.BAD_REQUEST.value()).body(errorMessage.getBytes()).request(Request
						.create(Request.HttpMethod.GET, "/api/comments/by-blog/{id}", Map.of(), null, null, null))
						.build());

		Mockito.when(commentOpenFeign.getCommentsByBlog(1L)).thenThrow(feignException);

		ApiResponse<Object, Object> result = userCommentService.getCommentsByBlog(1L);

		assertNotNull(result);
		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
		assertNotNull(result.geterror());

	}

	@Test
	@Order(5)
	void testGetCommentsByBlogSuccess() {

		CommentRequestDTO commentRequestDTO2 = CommentRequestDTOBuilder.withAllDummy().setBlogId(1L)
				.setContent("Content comment").setUserId(1L).build2();

		List<CommentRequestDTO> comments = new ArrayList<>();
		comments.add(commentRequestDTO);
		comments.add(commentRequestDTO2);

		ApiResponse<Object, Object> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), comments, null);

		ResponseEntity<ApiResponse<?, ?>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);
		Mockito.when(commentOpenFeign.getCommentsByBlog(1L)).thenReturn(responseEntity);

		ApiResponse<Object, Object> result = userCommentService.getCommentsByBlog(1L);

		List<CommentRequestDTO> commentResponse = (List<CommentRequestDTO>) result.getData();

		assertNotNull(result);
		assertEquals(2, commentResponse.size());
		assertEquals(200, result.getStatus());
		assertEquals(null, result.geterror());

	}

	@Test
	@Order(8)
	void testGetCommentsByUserExcepcion() {

		String errorMessage = "{" + "\"status\": 400," + "\"data\": null," + "\"error\": {"
				+ "\"error\": \"Comment Error\"," + "\"message\": \"A error ocurred\","
				+ "\"timeStamp\": \"2024-10-15T17:12:46.8787456\"" + "},"
				+ "\"timeStamp\": \"2024-10-15T17:12:46.8787456\"" + "}";

		FeignException feignException = FeignException.errorStatus("Bad Request",
				Response.builder().status(HttpStatus.BAD_REQUEST.value()).body(errorMessage.getBytes()).request(Request
						.create(Request.HttpMethod.GET, "/api/comments/by-user/{id}", Map.of(), null, null, null))
						.build());

		Mockito.when(commentOpenFeign.getCommentsByUser(1L)).thenThrow(feignException);

		ApiResponse<Object, Object> result = userCommentService.getCommentsByUser(1L);

		assertNotNull(result);
		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
		assertNotNull(result.geterror());

	}

	@Test
	@Order(7)
	void testGetCommentsByUserSuccess() {

		CommentRequestDTO commentRequestDTO2 = CommentRequestDTOBuilder.withAllDummy().setBlogId(1L)
				.setContent("Content comment").setUserId(1L).build2();

		List<CommentRequestDTO> comments = new ArrayList<>();
		comments.add(commentRequestDTO);
		comments.add(commentRequestDTO2);

		ApiResponse<Object, Object> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), comments, null);

		ResponseEntity<ApiResponse<?, ?>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);
		Mockito.when(commentOpenFeign.getCommentsByUser(1L)).thenReturn(responseEntity);

		ApiResponse<Object, Object> result = userCommentService.getCommentsByUser(1L);

		List<CommentRequestDTO> commentResponse = (List<CommentRequestDTO>) result.getData();

		assertNotNull(result);
		assertEquals(2, commentResponse.size());
		assertEquals(200, result.getStatus());
		assertEquals(null, result.geterror());

	}

}
