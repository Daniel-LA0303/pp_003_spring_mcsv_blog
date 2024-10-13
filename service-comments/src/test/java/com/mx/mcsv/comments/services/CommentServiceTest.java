package com.mx.mcsv.comments.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

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

import com.fasterxml.jackson.databind.ObjectMapper;
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

	@Autowired
	private ObjectMapper objectMapper;

	@LocalServerPort
	private int port;

	Comment commentTest;

	@Test
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

	@BeforeEach
	void setUp() {
		commentTest = CommentBuilder.withAllDymmuy().build();
	}

	@Test

	void testSaveComment_Success() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// Crea un objeto Comment válido
		Comment validComment = new Comment();
		validComment.setUserId(1L); // Establece un userId válido
		validComment.setBlogId(1L); // Establece un blogId válido
		validComment.setContent("Este es un comentario de prueba"); // Contenido válido

		HttpEntity<Comment> requestEntity = new HttpEntity<>(validComment, headers);

		ResponseEntity<ApiResponse<CommentDTO, String>> response = testRestTemplate.exchange("/api/comments",
				HttpMethod.POST, requestEntity, new ParameterizedTypeReference<ApiResponse<CommentDTO, String>>() {
				});

		// Verifica que el estado de la respuesta sea CREATED
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		ApiResponse<CommentDTO, String> apiResponse = response.getBody();
		assertNotNull(apiResponse);

		// Verifica que el estado del API sea 201
		assertEquals(201, apiResponse.getStatus());
		assertNull(apiResponse.geterror()); // No debe haber errores en caso de éxito

		CommentDTO commentDTO = apiResponse.getData();
		assertNotNull(commentDTO); // El objeto comentario devuelto no debe ser nulo
		assertEquals(validComment.getUserId(), commentDTO.getUserId()); // Verifica el userId
		assertEquals(validComment.getBlogId(), commentDTO.getBlogId()); // Verifica el blogId
		assertEquals(validComment.getContent(), commentDTO.getContent()); // Verifica el contenido

		// Verifica la timestamp si es necesario
		assertNotNull(apiResponse.getTimeStamp());
	}

}
