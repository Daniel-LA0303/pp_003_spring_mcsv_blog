package com.mx.mcsv.blog.services;

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

import com.mx.mcsv.blog.dto.ApiResponse;
import com.mx.mcsv.blog.entity.Blog;
import com.mx.mcsv.blog.exception.ErrorDetail;
import com.mx.mcsv.blog.utils.BlogBuilder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/import.sql")
@ActiveProfiles("test")
public class BlogServiceTestException {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@LocalServerPort
	private int port;

	Blog invalidBlog;

	Blog validBlog;

	Blog duplicateBlog;

	HttpHeaders headers;

	@Test
	void deleteBlogTestNotFound() {
		long nonexistentBlogId = 999L;
		long userId = 1L;

		ParameterizedTypeReference<ApiResponse<String, ErrorDetail>> responseType = new ParameterizedTypeReference<ApiResponse<String, ErrorDetail>>() {
		};

		ResponseEntity<ApiResponse<String, ErrorDetail>> response = testRestTemplate
				.exchange("/api/blogs/" + nonexistentBlogId + "/" + userId, HttpMethod.DELETE, null, responseType);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		ApiResponse<String, ErrorDetail> apiResponse = response.getBody();
		assertNotNull(apiResponse);

		assertEquals(404, apiResponse.getStatus());
		assertNotNull(apiResponse.geterror());
		assertNull(apiResponse.getData());

		ErrorDetail errorDetail = apiResponse.geterror();
		assertEquals("Blog Error", errorDetail.getError());
		assertEquals("Blog not found with id: " + nonexistentBlogId, errorDetail.getMessage());
		assertNotNull(errorDetail.getTimeStamp());
	}

	@Test
	void deleteBlogTestUserIdMismatch() {
		long blogId = 2L;
		long incorrectUserId = 999L;

		ParameterizedTypeReference<ApiResponse<String, ErrorDetail>> responseType = new ParameterizedTypeReference<ApiResponse<String, ErrorDetail>>() {
		};

		ResponseEntity<ApiResponse<String, ErrorDetail>> response = testRestTemplate
				.exchange("/api/blogs/" + blogId + "/" + incorrectUserId, HttpMethod.DELETE, null, responseType);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ApiResponse<String, ErrorDetail> apiResponse = response.getBody();
		assertNotNull(apiResponse);

		assertEquals(400, apiResponse.getStatus());
		assertNotNull(apiResponse.geterror());
		assertNull(apiResponse.getData());

		ErrorDetail errorDetail = apiResponse.geterror();
		assertEquals("Blog Error", errorDetail.getError());
		assertEquals("You do not have permissions to delete blogs of others users or user not found: ",
				errorDetail.getMessage());
		assertNotNull(errorDetail.getTimeStamp());
	}

	@Test
	void getBlogTestNotFound() {
		long nonexistentBlogId = 999L;

		ParameterizedTypeReference<ApiResponse<String, ErrorDetail>> responseType = new ParameterizedTypeReference<ApiResponse<String, ErrorDetail>>() {
		};

		ResponseEntity<ApiResponse<String, ErrorDetail>> response = testRestTemplate
				.exchange("/api/blogs/" + nonexistentBlogId, HttpMethod.GET, null, responseType);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		ApiResponse<String, ErrorDetail> apiResponse = response.getBody();
		assertNotNull(apiResponse);

		assertEquals(404, apiResponse.getStatus());
		assertNotNull(apiResponse.geterror());
		assertNull(apiResponse.getData());

		ErrorDetail errorDetail = apiResponse.geterror();
		assertEquals("Blog Error", errorDetail.getError());
		assertEquals("Blog not found with id: " + nonexistentBlogId, errorDetail.getMessage());
		assertNotNull(errorDetail.getTimeStamp());
	}

	@BeforeEach
	void setUp() {

		validBlog = BlogBuilder.withAllDummy().setTitle(
				"Sustainability in Business', 'How companies can integrate sustainability into their business models.")
				.setContent("Exploring the impact of technology on daily life and future trends.").setUserId(1L)
				.setId(null).build2();

		invalidBlog = BlogBuilder.withAllDummy().setTitle("").setDescription(null).setContent("").setUserId(null)
				.setId(null).build2();

		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	}

	@Test
	void testSaveBlogValidationErrors() {
		HttpEntity<Blog> requestEntity = new HttpEntity<>(invalidBlog, headers);

		ResponseEntity<ApiResponse<Map<String, String>, Map<String, String>>> response = testRestTemplate.exchange(
				"/api/blogs", HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<ApiResponse<Map<String, String>, Map<String, String>>>() {
				});

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ApiResponse<Map<String, String>, Map<String, String>> apiResponse = response.getBody();
		assertNotNull(apiResponse);

		assertEquals(400, apiResponse.getStatus());

		assertNull(apiResponse.getData());

		Map<String, String> errorMap = apiResponse.geterror();
		assertNotNull(errorMap);

		assertTrue(errorMap.containsKey("title"));
		assertTrue(errorMap.containsKey("description"));
		assertTrue(errorMap.containsKey("content"));
		assertTrue(errorMap.containsKey("userId"));

		System.out.println("******");
		System.out.println(errorMap.get("title"));

		assertEquals("The field title must not be blank", errorMap.get("title"));
		assertEquals("The field description must not be blank", errorMap.get("description"));
		assertEquals("The field content must not be blank", errorMap.get("content"));
		assertEquals("The field userId must not be null", errorMap.get("userId"));

		assertNotNull(apiResponse.getTimeStamp());
	}

	@Test
	void testUpdateBlogNotFound() {
		HttpEntity<Blog> requestEntity = new HttpEntity<>(validBlog, headers);

		ResponseEntity<ApiResponse<String, ErrorDetail>> response = testRestTemplate.exchange("/api/blogs/999",
				HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<ApiResponse<String, ErrorDetail>>() {
				});

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		ApiResponse<String, ErrorDetail> apiResponse = response.getBody();
		assertNotNull(apiResponse);

		assertEquals(404, apiResponse.getStatus());
		assertNull(apiResponse.getData());

		ErrorDetail errorDetail = apiResponse.geterror();
		assertNotNull(errorDetail);
		assertEquals("Blog Error", errorDetail.getError());
		assertEquals("Blog not found with id: 999", errorDetail.getMessage());

		assertNotNull(apiResponse.getTimeStamp());
	}

	@Test
	void testupdateBlogValidationErrors() {
		HttpEntity<Blog> requestEntity = new HttpEntity<>(invalidBlog, headers);

		ResponseEntity<ApiResponse<Map<String, String>, Map<String, String>>> response = testRestTemplate.exchange(
				"/api/blogs/1", HttpMethod.PUT, requestEntity,
				new ParameterizedTypeReference<ApiResponse<Map<String, String>, Map<String, String>>>() {
				});

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ApiResponse<Map<String, String>, Map<String, String>> apiResponse = response.getBody();
		assertNotNull(apiResponse);

		assertEquals(400, apiResponse.getStatus());

		assertNull(apiResponse.getData());

		Map<String, String> errorMap = apiResponse.geterror();
		assertNotNull(errorMap);

		assertTrue(errorMap.containsKey("title"));
		assertTrue(errorMap.containsKey("description"));
		assertTrue(errorMap.containsKey("content"));
		assertTrue(errorMap.containsKey("userId"));

		assertEquals("The field title must not be blank", errorMap.get("title"));
		assertEquals("The field description must not be blank", errorMap.get("description"));
		assertEquals("The field content must not be blank", errorMap.get("content"));
		assertEquals("The field userId must not be null", errorMap.get("userId"));

		assertNotNull(apiResponse.getTimeStamp());
	}

}
