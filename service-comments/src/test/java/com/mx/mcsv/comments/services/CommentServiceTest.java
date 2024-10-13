package com.mx.mcsv.comments.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.mx.mcsv.comments.dto.ApiResponse;
import com.mx.mcsv.comments.dto.CommentDTO;
import com.mx.mcsv.comments.entity.Comment;
import com.mx.mcsv.comments.utils.CommentBuilder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/import.sql")
@ActiveProfiles("test")
class CommentServiceTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@LocalServerPort
	private int port;

	Comment commentTest;

	Comment validComment;

	HttpHeaders headers;

	@BeforeEach
	void setUp() {
		commentTest = CommentBuilder.withAllDymmuy().build();

		validComment = CommentBuilder.withAllDymmuy().setBlogId(5L).setId(null).setUserId(1L)
				.setContent("Este es un comentario de prueba").build();

		headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
	}

	@Test
	@Order(1)
	void getAllCommentsTest() {
		ResponseEntity<ApiResponse> response = testRestTemplate.getForEntity("/api/comments", ApiResponse.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		ApiResponse<List<CommentDTO>, String> apiResponse = response.getBody();
		assertNotNull(apiResponse);
		assertEquals(200, apiResponse.getStatus());
		assertNull(apiResponse.geterror());

		List<CommentDTO> comments = apiResponse.getData();
		assertNotNull(comments);
		assertNotNull(apiResponse.getTimeStamp());
	}

	@Test
	@Order(2)
	void getCommentsByBlog() {
		ResponseEntity<ApiResponse> response = testRestTemplate.getForEntity("/api/comments/by-blog/5",
				ApiResponse.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		ApiResponse<List<CommentDTO>, String> apiResponse = response.getBody();
		assertNotNull(apiResponse);
		assertEquals(200, apiResponse.getStatus());
		assertNull(apiResponse.geterror());

		List<CommentDTO> comments = apiResponse.getData();
		System.out.println(comments);
		assertNotNull(comments);
		assertNotNull(apiResponse.getTimeStamp());
	}

	@Test
	@Order(3)
	void getCommentsByUser() {
		ResponseEntity<ApiResponse> response = testRestTemplate.getForEntity("/api/comments/by-user/1",
				ApiResponse.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		ApiResponse<List<CommentDTO>, String> apiResponse = response.getBody();
		assertNotNull(apiResponse);
		assertEquals(200, apiResponse.getStatus());
		assertNull(apiResponse.geterror());

		List<CommentDTO> comments = apiResponse.getData();
		System.out.println(comments);
		assertNotNull(comments);
		assertNotNull(apiResponse.getTimeStamp());
	}

	@Test
	@Order(4)
	void getCommentTest() {
		ParameterizedTypeReference<ApiResponse<CommentDTO, String>> responseType = new ParameterizedTypeReference<ApiResponse<CommentDTO, String>>() {
		};

		ResponseEntity<ApiResponse<CommentDTO, String>> response = testRestTemplate.exchange("/api/comments/1",
				HttpMethod.GET, null, responseType);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		ApiResponse<CommentDTO, String> apiResponse = response.getBody();

		assertNotNull(apiResponse);
		assertEquals(200, apiResponse.getStatus());
		assertNull(apiResponse.geterror());

		CommentDTO comment = apiResponse.getData();
		assertEquals(commentTest.getId(), comment.getId());
		assertEquals(commentTest.getContent(), comment.getContent());
		assertEquals(commentTest.getCreatedAt(), comment.getCreatedAt());

		assertNotNull(apiResponse.getTimeStamp());
	}

	@Test
	@Order(7)
	void deleteCommentTest() {

		ParameterizedTypeReference<ApiResponse<String, String>> responseType = new ParameterizedTypeReference<ApiResponse<String, String>>() {
		};

		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<ApiResponse<String, String>> response = testRestTemplate.exchange("/api/comments/{id}/{userId}",
				HttpMethod.DELETE, requestEntity, responseType, 1L, 1L);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		ApiResponse<String, String> apiResponse = response.getBody();
		assertNotNull(apiResponse);
		assertEquals(200, apiResponse.getStatus());
		assertEquals("Comment deleted", apiResponse.getData());
		assertNull(apiResponse.geterror());

		assertNotNull(apiResponse.getTimeStamp());

	}

	@Test
	@Order(5)
	void saveCommentTest() {

		HttpEntity<Comment> requestEntity = new HttpEntity<>(validComment, headers);

		ResponseEntity<ApiResponse<CommentDTO, String>> response = testRestTemplate.exchange("/api/comments",
				HttpMethod.POST, requestEntity, new ParameterizedTypeReference<ApiResponse<CommentDTO, String>>() {
				});

		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		ApiResponse<CommentDTO, String> apiResponse = response.getBody();
		assertNotNull(apiResponse);
		assertEquals(201, apiResponse.getStatus());
		assertNull(apiResponse.geterror());

		// CommentDTO commentDTO = apiResponse.getData();
		CommentDTO commentDTO = new CommentDTO(apiResponse.getData().getId(), apiResponse.getData().getUserId(),
				apiResponse.getData().getBlogId(), apiResponse.getData().getContent(),
				apiResponse.getData().getCreatedAt());
		assertNotNull(commentDTO);
		assertEquals(validComment.getUserId(), commentDTO.getUserId());
		assertEquals(validComment.getBlogId(), commentDTO.getBlogId());
		assertEquals(validComment.getContent(), commentDTO.getContent());

		assertNotNull(apiResponse.getTimeStamp());
	}

	@Test
	@Order(6)
	void updateCommentTest() {

		HttpEntity<Comment> requestEntity = new HttpEntity<>(validComment, headers);

		ResponseEntity<ApiResponse<CommentDTO, String>> response = testRestTemplate.exchange("/api/comments/1",
				HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<ApiResponse<CommentDTO, String>>() {
				});

		assertEquals(HttpStatus.OK, response.getStatusCode());

		ApiResponse<CommentDTO, String> apiResponse = response.getBody();
		assertNotNull(apiResponse);
		assertEquals(200, apiResponse.getStatus());
		assertNull(apiResponse.geterror());

		CommentDTO commentDTO = apiResponse.getData();

		assertNotNull(commentDTO);
		System.out.println("userId valid = " + validComment.getUserId() + " userid response" + commentDTO.getUserId());
		assertEquals(validComment.getUserId(), commentDTO.getUserId());
		assertEquals(validComment.getBlogId(), commentDTO.getBlogId());
		assertEquals(validComment.getContent(), commentDTO.getContent());

		assertNotNull(apiResponse.getTimeStamp());

	}

}
