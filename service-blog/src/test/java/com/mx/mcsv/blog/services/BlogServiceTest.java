package com.mx.mcsv.blog.services;

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

import com.mx.mcsv.blog.dto.ApiResponse;
import com.mx.mcsv.blog.dto.BlogDTO;
import com.mx.mcsv.blog.entity.Blog;
import com.mx.mcsv.blog.exception.ErrorDetail;
import com.mx.mcsv.blog.utils.BlogBuilder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/import.sql")
@ActiveProfiles("test")
public class BlogServiceTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@LocalServerPort
	private int port;

	Blog blogTest;

	Blog validBlog;

	Blog validBlog2;

	Blog duplicateBlog;

	HttpHeaders headers;

	@Test
	@Order(8)
	void deleteCommentTest() {

		ParameterizedTypeReference<ApiResponse<String, String>> responseType = new ParameterizedTypeReference<ApiResponse<String, String>>() {
		};

		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<ApiResponse<String, String>> response = testRestTemplate.exchange("/api/blogs/{id}/{userId}",
				HttpMethod.DELETE, requestEntity, responseType, 1L, 1L);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		ApiResponse<String, String> apiResponse = response.getBody();
		assertNotNull(apiResponse);
		System.out.println("****** dele");
		System.out.println(apiResponse.geterror());
		assertEquals(200, apiResponse.getStatus());
		assertEquals("Blog deleted", apiResponse.getData());
		assertNull(apiResponse.geterror());

		assertNotNull(apiResponse.getTimeStamp());

	}

	@Test
	@Order(1)
	void getAllBlogsTest() {
		ResponseEntity<ApiResponse> response = testRestTemplate.getForEntity("/api/blogs", ApiResponse.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		ApiResponse<List<BlogDTO>, String> apiResponse = response.getBody();
		assertNotNull(apiResponse);
		assertEquals(200, apiResponse.getStatus());
		assertNull(apiResponse.geterror());

		List<BlogDTO> blogs = apiResponse.getData();
		assertNotNull(blogs);
		assertNotNull(apiResponse.getTimeStamp());
	}

	@Test
	@Order(3)
	void getBlogsByUserTest() {
		ResponseEntity<ApiResponse> response = testRestTemplate.getForEntity("/api/blogs/get-blogs-by-id/1",
				ApiResponse.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		ApiResponse<List<BlogDTO>, String> apiResponse = response.getBody();
		assertNotNull(apiResponse);
		assertEquals(200, apiResponse.getStatus());
		assertNull(apiResponse.geterror());

		List<BlogDTO> blogs = apiResponse.getData();
		System.out.println(blogs);
		assertNotNull(blogs);
		assertNotNull(apiResponse.getTimeStamp());
	}

	@Test
	@Order(2)
	void getBlogTest() {
		ParameterizedTypeReference<ApiResponse<BlogDTO, Object>> responseType = new ParameterizedTypeReference<ApiResponse<BlogDTO, Object>>() {
		};

		ResponseEntity<ApiResponse<BlogDTO, Object>> response = testRestTemplate.exchange("/api/blogs/1",
				HttpMethod.GET, null, responseType);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		ApiResponse<BlogDTO, Object> apiResponse = response.getBody();
		assertNotNull(apiResponse);
		assertEquals(200, apiResponse.getStatus());
		assertNull(apiResponse.geterror());

		BlogDTO blogDTO = apiResponse.getData();

		assertEquals(blogDTO.getId(), blogTest.getId());
		assertEquals(blogDTO.getContent(), blogTest.getContent());
		assertEquals(blogDTO.getCreatedAt(), blogTest.getCreatedAt());
		assertEquals(blogDTO.getDescription(), blogTest.getDescription());
		assertEquals(blogDTO.getStatus(), blogTest.getStatus());
		assertEquals(blogDTO.getTitle(), blogTest.getTitle());
		assertEquals(blogDTO.getUserId(), blogTest.getUserId());

		assertNotNull(apiResponse.getTimeStamp());
	}

	@Test
	@Order(4)
	void saveCommentTest() {

		HttpEntity<Blog> requestEntity = new HttpEntity<>(validBlog, headers);

		ResponseEntity<ApiResponse<BlogDTO, String>> response = testRestTemplate.exchange("/api/blogs", HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<ApiResponse<BlogDTO, String>>() {
				});

		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		ApiResponse<BlogDTO, String> apiResponse = response.getBody();
		assertNotNull(apiResponse);
		assertEquals(201, apiResponse.getStatus());
		assertNull(apiResponse.geterror());

		BlogDTO blogDTO = new BlogDTO(apiResponse.getData().getId(), apiResponse.getData().getTitle(),
				apiResponse.getData().getDescription(), apiResponse.getData().getContent(),
				apiResponse.getData().getUserId(), apiResponse.getData().getStatus(),
				apiResponse.getData().getCreatedAt());

		assertNotNull(blogDTO);
		assertEquals(blogDTO.getContent(), validBlog.getContent());
		assertEquals(blogDTO.getDescription(), validBlog.getDescription());
		assertEquals(blogDTO.getStatus(), validBlog.getStatus());
		assertEquals(blogDTO.getTitle(), validBlog.getTitle());
		assertEquals(blogDTO.getUserId(), validBlog.getUserId());

		assertNotNull(apiResponse.getTimeStamp());
	}

	@BeforeEach
	void setUp() {
		blogTest = BlogBuilder.withAllDummy().build();

		validBlog = BlogBuilder.withAllDummy().setTitle("Innovations in Technology")
				.setContent("Exploring the impact of technology on daily life and future trends.").setUserId(1L)
				.setId(null).build();

		validBlog2 = BlogBuilder.withAllDummy().setTitle("The Future of Artificial Intelligence")
				.setContent("A deep dive into the advancements and ethical considerations of AI.").setUserId(1L)
				.build();

		duplicateBlog = BlogBuilder.withAllDummy().setTitle("duplicado").setContent("duplicado").setUserId(1L)
				.setId(null).build();

		headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
	}

	@Test
	@Order(6)
	void testSaveBlogDuplicateTitle() {

		HttpEntity<Blog> requestEntity = new HttpEntity<>(validBlog, headers);

		ResponseEntity<ApiResponse<String, ErrorDetail>> response = testRestTemplate.exchange("/api/blogs",
				HttpMethod.POST, requestEntity, new ParameterizedTypeReference<ApiResponse<String, ErrorDetail>>() {
				});

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ApiResponse<String, ErrorDetail> apiResponse = response.getBody();
		assertNotNull(apiResponse);

		assertEquals(400, apiResponse.getStatus());
		assertNull(apiResponse.getData());
		assertNotNull(apiResponse.geterror());

		ErrorDetail errorMessage = apiResponse.geterror();
		assertNotNull(errorMessage);
		assertEquals("A blog with this title already exists", errorMessage.getMessage());

		assertNotNull(apiResponse.getTimeStamp());
	}

	@Test
	@Order(7)
	void testUpdateBlogDuplicateTitle() {

		HttpEntity<Blog> requestEntity = new HttpEntity<>(validBlog, headers);

		ResponseEntity<ApiResponse<String, ErrorDetail>> response = testRestTemplate.exchange("/api/blogs/1",
				HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<ApiResponse<String, ErrorDetail>>() {
				});

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ApiResponse<String, ErrorDetail> apiResponse = response.getBody();
		assertNotNull(apiResponse);

		assertEquals(400, apiResponse.getStatus());
		assertNull(apiResponse.getData());
		assertNotNull(apiResponse.geterror());

		ErrorDetail errorMessage = apiResponse.geterror();
		assertNotNull(errorMessage);
		assertEquals("A blog with this title already exists", errorMessage.getMessage());

		assertNotNull(apiResponse.getTimeStamp());
	}

	@Test
	@Order(5)
	void updateBlogTest() {

		HttpEntity<Blog> requestEntity = new HttpEntity<>(validBlog2, headers);
		System.out.println("*****update");
		ResponseEntity<ApiResponse<BlogDTO, String>> response = testRestTemplate.exchange("/api/blogs/1",
				HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<ApiResponse<BlogDTO, String>>() {
				});

		assertEquals(HttpStatus.OK, response.getStatusCode());

		ApiResponse<BlogDTO, String> apiResponse = response.getBody();
		assertNotNull(apiResponse);
		assertEquals(200, apiResponse.getStatus());
		assertNull(apiResponse.geterror());

		BlogDTO blogDTO = apiResponse.getData();

		assertNotNull(blogDTO);
		assertEquals(blogDTO.getContent(), validBlog2.getContent());
		assertEquals(blogDTO.getDescription(), validBlog2.getDescription());
		assertEquals(blogDTO.getStatus(), validBlog2.getStatus());
		assertEquals(blogDTO.getId(), validBlog2.getId());
		assertEquals(blogDTO.getTitle(), validBlog2.getTitle());
		assertEquals(blogDTO.getUserId(), validBlog2.getUserId());

		assertNotNull(apiResponse.getTimeStamp());
	}

}
