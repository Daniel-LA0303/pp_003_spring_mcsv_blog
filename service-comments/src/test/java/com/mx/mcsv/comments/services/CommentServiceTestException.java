package com.mx.mcsv.comments.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
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
import com.mx.mcsv.comments.entity.Comment;
import com.mx.mcsv.comments.exceptions.ErrorDetail;
import com.mx.mcsv.comments.utils.CommentBuilder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/import.sql")
@ActiveProfiles("test")
public class CommentServiceTestException {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@LocalServerPort
	private int port;

	Comment invalidComment;

	Comment validComment;

	HttpHeaders headers;

	@BeforeEach
	void setUp() {

		validComment = CommentBuilder.withAllDymmuy().setBlogId(5L).setId(null).setUserId(1L)
				.setContent("Este es un comentario de prueba").build2();

		invalidComment = CommentBuilder.withAllDymmuy().setBlogId(null).setId(null).setUserId(null).setContent("")
				.build2();

		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	}

	@Test
	void deleteCommentTestNotFound() {
		long nonexistentCommentId = 999L;
		long userId = 1L;

		ParameterizedTypeReference<ApiResponse<String, ErrorDetail>> responseType = new ParameterizedTypeReference<ApiResponse<String, ErrorDetail>>() {
		};

		ResponseEntity<ApiResponse<String, ErrorDetail>> response = testRestTemplate.exchange(
				"/api/comments/" + nonexistentCommentId + "/" + userId, HttpMethod.DELETE, null, responseType);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		ApiResponse<String, ErrorDetail> apiResponse = response.getBody();
		assertNotNull(apiResponse);

		assertEquals(404, apiResponse.getStatus());
		assertNotNull(apiResponse.geterror());
		assertNull(apiResponse.getData());

		ErrorDetail errorDetail = apiResponse.geterror();
		assertEquals("Comment Error", errorDetail.getError());
		assertEquals("Comment not found with id: " + nonexistentCommentId, errorDetail.getMessage());
		assertNotNull(errorDetail.getTimeStamp());
	}

	@Test
	void deleteCommentTestUserIdMismatch() {
		long commentId = 1L; // Id de un comentario existente
		long incorrectUserId = 2L; // Id de un usuario que no es el dueño del comentario

		ParameterizedTypeReference<ApiResponse<String, ErrorDetail>> responseType = new ParameterizedTypeReference<ApiResponse<String, ErrorDetail>>() {
		};

		ResponseEntity<ApiResponse<String, ErrorDetail>> response = testRestTemplate
				.exchange("/api/comments/" + commentId + "/" + incorrectUserId, HttpMethod.DELETE, null, responseType);

		// Verifica que el código de estado sea 400 BAD_REQUEST
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		// Obtén el cuerpo de la respuesta y asegúrate de que no sea nulo
		ApiResponse<String, ErrorDetail> apiResponse = response.getBody();
		assertNotNull(apiResponse);

		// Verifica que el status dentro de ApiResponse sea 400
		assertEquals(400, apiResponse.getStatus());
		assertNotNull(apiResponse.geterror());
		assertNull(apiResponse.getData());

		// Verifica los detalles del error
		ErrorDetail errorDetail = apiResponse.geterror();
		assertEquals("Comment Error", errorDetail.getError());
		assertEquals("You do not have permissions to delete comments of others users", errorDetail.getMessage());
		assertNotNull(errorDetail.getTimeStamp());
	}

	@Test
	void getCommentTestNotFound() {
		long nonexistentCommentId = 999L;

		ParameterizedTypeReference<ApiResponse<String, ErrorDetail>> responseType = new ParameterizedTypeReference<ApiResponse<String, ErrorDetail>>() {
		};

		ResponseEntity<ApiResponse<String, ErrorDetail>> response = testRestTemplate
				.exchange("/api/comments/" + nonexistentCommentId, HttpMethod.GET, null, responseType);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		ApiResponse<String, ErrorDetail> apiResponse = response.getBody();
		assertNotNull(apiResponse);

		assertEquals(404, apiResponse.getStatus());
		assertNotNull(apiResponse.geterror());
		assertNull(apiResponse.getData());

		ErrorDetail errorDetail = apiResponse.geterror();
		assertEquals("Comment Error", errorDetail.getError());
		assertEquals("Comment not found with id: " + nonexistentCommentId, errorDetail.getMessage());
		assertNotNull(errorDetail.getTimeStamp());
	}

	@Test
	void testSaveCommentValidationErrors() {
		HttpEntity<Comment> requestEntity = new HttpEntity<>(invalidComment, headers);

		ResponseEntity<ApiResponse<Map<String, String>, Map<String, String>>> response = testRestTemplate.exchange(
				"/api/comments", HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<ApiResponse<Map<String, String>, Map<String, String>>>() {
				});

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ApiResponse<Map<String, String>, Map<String, String>> apiResponse = response.getBody();
		assertNotNull(apiResponse);

		assertEquals(400, apiResponse.getStatus());
		assertNull(apiResponse.getData());
		Map<String, String> errorMap = apiResponse.geterror();
		assertNotNull(errorMap);
		assertTrue(errorMap.containsKey("userId"));
		assertTrue(errorMap.containsKey("blogId"));
		assertTrue(errorMap.containsKey("content"));

		assertEquals("The field userId must not be null", errorMap.get("userId"));
		assertEquals("The field blogId must not be null", errorMap.get("blogId"));
		assertEquals("The field content must not be blank", errorMap.get("content"));

		assertNotNull(apiResponse.getTimeStamp());
	}

	@Test
	void testUpdateCommentValidationErrors() {
		HttpEntity<Comment> requestEntity = new HttpEntity<>(invalidComment, headers);

		ResponseEntity<ApiResponse<Map<String, String>, Map<String, String>>> response = testRestTemplate.exchange(
				"/api/comments/1", HttpMethod.PUT, requestEntity,
				new ParameterizedTypeReference<ApiResponse<Map<String, String>, Map<String, String>>>() {
				});

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ApiResponse<Map<String, String>, Map<String, String>> apiResponse = response.getBody();
		assertNotNull(apiResponse);

		assertEquals(400, apiResponse.getStatus());
		assertNull(apiResponse.getData());
		Map<String, String> errorMap = apiResponse.geterror();
		assertNotNull(errorMap);
		assertTrue(errorMap.containsKey("userId"));
		assertTrue(errorMap.containsKey("blogId"));
		assertTrue(errorMap.containsKey("content"));

		assertEquals("The field userId must not be null", errorMap.get("userId"));
		assertEquals("The field blogId must not be null", errorMap.get("blogId"));
		assertEquals("The field content must not be blank", errorMap.get("content"));

		assertNotNull(apiResponse.getTimeStamp());
	}

	@Test
	void testUpdateCommentNotFound() {
		HttpEntity<Comment> requestEntity = new HttpEntity<>(validComment, headers);

		ResponseEntity<ApiResponse<String, ErrorDetail>> response = testRestTemplate.exchange("/api/comments/999",
				HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<ApiResponse<String, ErrorDetail>>() {
				});

		// Verifica que el código de estado sea 404 NOT_FOUND
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		// Obtén el cuerpo de la respuesta y asegúrate de que no sea nulo
		ApiResponse<String, ErrorDetail> apiResponse = response.getBody();
		assertNotNull(apiResponse);

		// Verifica que el status dentro de ApiResponse sea 404
		assertEquals(404, apiResponse.getStatus());
		assertNull(apiResponse.getData());

		// Verifica los detalles del error
		ErrorDetail errorDetail = apiResponse.geterror();
		assertNotNull(errorDetail);
		assertEquals("Comment Error", errorDetail.getError());
		assertEquals("Comment not found with id: 999", errorDetail.getMessage());

		assertNotNull(apiResponse.getTimeStamp());
	}

}
