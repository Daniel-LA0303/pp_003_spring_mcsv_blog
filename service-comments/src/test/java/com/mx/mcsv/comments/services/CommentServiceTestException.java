package com.mx.mcsv.comments.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

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
import com.mx.mcsv.comments.entity.Comment;
import com.mx.mcsv.comments.exceptions.ErrorDetail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/import.sql")
@ActiveProfiles("test")
public class CommentServiceTestException {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@LocalServerPort
	private int port;

	HttpHeaders headers;

	@Test
	void getCommentTestNotFound() {
		long nonexistentCommentId = 999L;

		// Utilizamos el tipo correcto para la respuesta
		ParameterizedTypeReference<ApiResponse<String, ErrorDetail>> responseType = new ParameterizedTypeReference<ApiResponse<String, ErrorDetail>>() {
		};

		ResponseEntity<ApiResponse<String, ErrorDetail>> response = testRestTemplate
				.exchange("/api/comments/" + nonexistentCommentId, HttpMethod.GET, null, responseType);

		// Verificamos que el estado de la respuesta sea NOT_FOUND
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		// Verificamos que el cuerpo de la respuesta no sea nulo
		ApiResponse<String, ErrorDetail> apiResponse = response.getBody();
		assertNotNull(apiResponse);

		// Verificamos que el estado del API sea el esperado para el caso no encontrado
		assertEquals(404, apiResponse.getStatus());
		assertNotNull(apiResponse.geterror()); // Aquí verificas que haya un error
		assertNull(apiResponse.getData()); // El data debe ser null si el comentario no existe

		// Verifica que el ErrorDetail contiene el mensaje esperado
		ErrorDetail errorDetail = apiResponse.geterror();
		assertEquals("Comment Error", errorDetail.getError());
		assertEquals("Comment not found with id: " + nonexistentCommentId, errorDetail.getMessage());
		assertNotNull(errorDetail.getTimeStamp()); // Verifica que la timestamp no sea nula
	}

	@Test
	// Asegúrate de que el orden sea adecuado según tus pruebas
	void testSaveComment_ValidationErrors() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// Crea un objeto Comment inválido
		Comment invalidComment = new Comment();
		invalidComment.setUserId(null); // Simula un userId nulo
		invalidComment.setBlogId(null); // Simula un blogId nulo
		invalidComment.setContent(""); // Contenido vacío

		HttpEntity<Comment> requestEntity = new HttpEntity<>(invalidComment, headers);

		ResponseEntity<ApiResponse<Map<String, String>, Map<String, String>>> response = testRestTemplate.exchange(
				"/api/comments", // Asegúrate de que la URL sea correcta para guardar un comentario
				HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<ApiResponse<Map<String, String>, Map<String, String>>>() {
				});

		// Verifica que el estado de la respuesta sea BAD_REQUEST
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ApiResponse<Map<String, String>, Map<String, String>> apiResponse = response.getBody();
		assertNotNull(apiResponse);

		// Verifica que el estado del API sea 400
		assertEquals(400, apiResponse.getStatus());
		assertNull(apiResponse.getData()); // La data debe ser null en caso de error de validación

		Map<String, String> errorMap = apiResponse.geterror();
		assertNotNull(errorMap);
		assertTrue(errorMap.containsKey("userId"));
		assertTrue(errorMap.containsKey("blogId"));
		assertTrue(errorMap.containsKey("content"));

		assertEquals("The field userId no debe ser nulo", errorMap.get("userId"));
		assertEquals("The field blogId no debe ser nulo", errorMap.get("blogId"));
		assertEquals("The field content no debe estar vacío", errorMap.get("content"));

		// Verifica la timestamp si es necesario
		assertNotNull(apiResponse.getTimeStamp());
	}

}
