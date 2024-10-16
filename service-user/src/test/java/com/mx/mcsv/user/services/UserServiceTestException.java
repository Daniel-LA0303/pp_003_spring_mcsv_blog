package com.mx.mcsv.user.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

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

import com.mx.mcsv.user.dto.ApiResponse;
import com.mx.mcsv.user.entity.User;
import com.mx.mcsv.user.exceptions.ErrorDetail;
import com.mx.mcsv.user.utils.UserBuilder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/import.sql")
@ActiveProfiles("test")
public class UserServiceTestException {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@LocalServerPort
	private int port;

	HttpHeaders headers;

	User validUser;

	User invalidUser;

	@Test
	void deleteUserTestNotFound() {
		long userId = 999L;

		ParameterizedTypeReference<ApiResponse<String, ErrorDetail>> responseType = new ParameterizedTypeReference<ApiResponse<String, ErrorDetail>>() {
		};

		ResponseEntity<ApiResponse<String, ErrorDetail>> response = testRestTemplate.exchange("/api/users/" + userId,
				HttpMethod.DELETE, null, responseType);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		ApiResponse<String, ErrorDetail> apiResponse = response.getBody();
		assertNotNull(apiResponse);

		assertEquals(404, apiResponse.getStatus());
		assertNotNull(apiResponse.geterror());
		assertNull(apiResponse.getData());

		ErrorDetail errorDetail = apiResponse.geterror();
		assertEquals("User Error", errorDetail.getError());
		assertEquals("User not found with id: " + userId, errorDetail.getMessage());
		assertNotNull(errorDetail.getTimeStamp());
	}

	@Test
	@Order(3)
	void getUserByEmailTest() {

		String email = "email999@email.com";

		ParameterizedTypeReference<ApiResponse<String, ErrorDetail>> responseType = new ParameterizedTypeReference<ApiResponse<String, ErrorDetail>>() {
		};

		ResponseEntity<ApiResponse<String, ErrorDetail>> response = testRestTemplate
				.exchange("/api/users/get-by-email/" + email, HttpMethod.GET, null, responseType);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		ApiResponse<String, ErrorDetail> apiResponse = response.getBody();

		assertNotNull(apiResponse);

		assertEquals(404, apiResponse.getStatus());

		ErrorDetail errorDetail = apiResponse.geterror();
		assertEquals("User Error", errorDetail.getError());
		assertEquals("User not found with email: " + email, errorDetail.getMessage());
		assertNotNull(errorDetail.getTimeStamp());
	}

	@Test
	void getUserTestNotFound() {
		long nonexistentBlogId = 999L;

		ParameterizedTypeReference<ApiResponse<String, ErrorDetail>> responseType = new ParameterizedTypeReference<ApiResponse<String, ErrorDetail>>() {
		};

		ResponseEntity<ApiResponse<String, ErrorDetail>> response = testRestTemplate
				.exchange("/api/users/" + nonexistentBlogId, HttpMethod.GET, null, responseType);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		ApiResponse<String, ErrorDetail> apiResponse = response.getBody();
		assertNotNull(apiResponse);

		assertEquals(404, apiResponse.getStatus());
		assertNotNull(apiResponse.geterror());
		assertNull(apiResponse.getData());

		ErrorDetail errorDetail = apiResponse.geterror();
		assertEquals("User Error", errorDetail.getError());
		assertEquals("User not found with id: " + nonexistentBlogId, errorDetail.getMessage());
		assertNotNull(errorDetail.getTimeStamp());
	}

	@BeforeEach
	void setUp() {

		validUser = UserBuilder.withAllDummy().setId(null).setEmail("email@email.com").setName("new.user")
				.setPassword("password").setUsername("new.user").build2();

		invalidUser = UserBuilder.withAllDummy().setId(null).setEmail("").setName("").setPassword("").setUsername("")
				.build2();

		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	}

	@Test
	void testSaveUserValidationErrors() {
		HttpEntity<User> requestEntity = new HttpEntity<>(invalidUser, headers);

		ResponseEntity<ApiResponse<Map<String, String>, Map<String, String>>> response = testRestTemplate.exchange(
				"/api/users/new-user", HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<ApiResponse<Map<String, String>, Map<String, String>>>() {
				});

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ApiResponse<Map<String, String>, Map<String, String>> apiResponse = response.getBody();
		assertNotNull(apiResponse);

		assertEquals(400, apiResponse.getStatus());

		assertNull(apiResponse.getData());

		Map<String, String> errorMap = apiResponse.geterror();
		assertNotNull(errorMap);

		assertTrue(errorMap.containsKey("name"));
		assertTrue(errorMap.containsKey("username"));
		assertTrue(errorMap.containsKey("email"));
		assertTrue(errorMap.containsKey("password"));

		assertEquals("The field name must not be blank", errorMap.get("name"));
		assertEquals("The field username must not be blank", errorMap.get("username"));
		assertEquals("The field email must not be blank", errorMap.get("email"));
		assertEquals("The field password must not be null", errorMap.get("password"));

		assertNotNull(apiResponse.getTimeStamp());
	}

	@Test
	void testUpdateUserNotFound() {
		HttpEntity<User> requestEntity = new HttpEntity<>(validUser, headers);

		ResponseEntity<ApiResponse<String, ErrorDetail>> response = testRestTemplate.exchange("/api/users/999",
				HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<ApiResponse<String, ErrorDetail>>() {
				});

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		ApiResponse<String, ErrorDetail> apiResponse = response.getBody();
		assertNotNull(apiResponse);

		assertEquals(404, apiResponse.getStatus());
		assertNull(apiResponse.getData());

		ErrorDetail errorDetail = apiResponse.geterror();
		assertNotNull(errorDetail);
		assertEquals("User Error", errorDetail.getError());
		assertEquals("User not found with id: 999", errorDetail.getMessage());

		assertNotNull(apiResponse.getTimeStamp());
	}

	@Test
	void testUpdateUserValidationErrors() {
		HttpEntity<User> requestEntity = new HttpEntity<>(invalidUser, headers);

		ResponseEntity<ApiResponse<Map<String, String>, Map<String, String>>> response = testRestTemplate.exchange(
				"/api/users/1", HttpMethod.PUT, requestEntity,
				new ParameterizedTypeReference<ApiResponse<Map<String, String>, Map<String, String>>>() {
				});

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ApiResponse<Map<String, String>, Map<String, String>> apiResponse = response.getBody();
		assertNotNull(apiResponse);

		assertEquals(400, apiResponse.getStatus());

		assertNull(apiResponse.getData());

		Map<String, String> errorMap = apiResponse.geterror();
		assertNotNull(errorMap);

		assertTrue(errorMap.containsKey("name"));
		assertTrue(errorMap.containsKey("username"));
		assertTrue(errorMap.containsKey("email"));
		assertTrue(errorMap.containsKey("password"));

		assertEquals("The field name must not be blank", errorMap.get("name"));
		assertEquals("The field username must not be blank", errorMap.get("username"));
		assertEquals("The field email must not be blank", errorMap.get("email"));
		assertEquals("The field password must not be null", errorMap.get("password"));

		assertNotNull(apiResponse.getTimeStamp());
	}

}
