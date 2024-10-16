package com.mx.mcsv.user.services.communication.blog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mx.mcsv.user.dto.ApiResponse;
import com.mx.mcsv.user.dto.BlogRequestDTO;
import com.mx.mcsv.user.exceptions.ErrorDetail;
import com.mx.mcsv.user.service.impl.UserBlogServiceImpl;
import com.mx.mcsv.user.utils.BlogRequestDTOBuilder;

@SpringBootTest
public class UserBlogServiceTest {

	@InjectMocks
	private UserBlogServiceImpl userBlogService;

	@Mock
	private RestTemplate restTemplate;

	BlogRequestDTO blogRequestDTO;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);

		blogRequestDTO = BlogRequestDTOBuilder.withAllDummy().build2();
	}

	// **** change to duplicate
	@Test
	@Order(4)
	void testCreateBlogHttpClientErrorException() {

		String errorMessage = "{" + "\"status\": 400," + "\"data\": null," + "\"error\": {"
				+ "\"error\": \"Blog Error\"," + "\"message\": \"A blog with this title already exists\","
				+ "\"timeStamp\": \"2024-10-15T17:12:46.8787456\"" + "},"
				+ "\"timeStamp\": \"2024-10-15T17:12:46.8787456\"" + "}";

		HttpClientErrorException httpClientErrorException = new HttpClientErrorException(HttpStatus.BAD_REQUEST,
				"Bad Request", errorMessage.getBytes(), null);

		Mockito.when(restTemplate.postForEntity(Mockito.eq("http://service-blog/api/blogs"), Mockito.eq(blogRequestDTO),
				Mockito.any(Class.class))).thenThrow(httpClientErrorException);

		ApiResponse<Object, Object> result = userBlogService.createBlog(blogRequestDTO);

		assertNotNull(result);
		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
		assertNotNull(result.geterror());

		Map<String, Object> errorDetails2 = (Map<String, Object>) result.geterror();
		ErrorDetail errorDetail = toErrorDetail(errorDetails2);

		assertNotNull(errorDetail);
		assertEquals("Blog Error", errorDetail.getError());
		assertEquals("A blog with this title already exists", errorDetail.getMessage());
	}

	@Test
	@Order(1)
	void testCreateBlogSuccess() {

		ApiResponse<Object, Object> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), blogRequestDTO, null);

		ResponseEntity<ApiResponse<Object, Object>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

		Mockito.when(restTemplate.postForEntity(Mockito.eq("http://service-blog/api/blogs"), Mockito.eq(blogRequestDTO),
				Mockito.any(Class.class))).thenReturn(responseEntity);

		ApiResponse<Object, Object> result = userBlogService.createBlog(blogRequestDTO);

		BlogRequestDTO blogResponse = (BlogRequestDTO) result.getData();

		assertNotNull(result);
		assertEquals(HttpStatus.OK.value(), result.getStatus());
		assertTrue(result.getData() instanceof BlogRequestDTO);

		assertEquals(blogRequestDTO.getContent(), blogResponse.getContent());
		assertEquals(blogRequestDTO.getDescription(), blogResponse.getDescription());
		assertEquals(blogRequestDTO.getStatus(), blogResponse.getStatus());
		assertEquals(blogRequestDTO.getTitle(), blogResponse.getTitle());
		assertEquals(blogRequestDTO.getUserId(), blogResponse.getUserId());
	}

	@Test
	@Order(6)
	void testDeleteBlogByUserException() {
		// Preparar el mensaje de error como se espera en el método
		String errorMessage = "{" + "\"status\": 400," + "\"data\": null," + "\"error\": {"
				+ "\"error\": \"User Error\"," + "\"message\": \"No blogs found for this user\","
				+ "\"timeStamp\": \"2024-10-15T17:12:46.8787456\"" + "},"
				+ "\"timeStamp\": \"2024-10-15T17:12:46.8787456\"" + "}";

		// Crear la excepción HttpClientErrorException
		HttpClientErrorException httpClientErrorException = new HttpClientErrorException(HttpStatus.BAD_REQUEST,
				"Bad Request", errorMessage.getBytes(), null);

		// Configurar el mock para lanzar la excepción
		Mockito.when(restTemplate.exchange(Mockito.eq("http://service-blog/api/blogs/{id}/{userId}"),
				Mockito.eq(HttpMethod.DELETE), Mockito.isNull(), Mockito.any(Class.class), Mockito.eq(1L),
				Mockito.eq(1L))).thenThrow(httpClientErrorException); // Aquí se utiliza thenThrow

		// Llamar al método que quieres probar
		ApiResponse<Object, Object> result = userBlogService.deteleBlogByUser(1L, 1L);

		// Verificar el resultado
		assertNotNull(result);
		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
		assertNotNull(result.geterror());

		Map<String, Object> errorDetails = (Map<String, Object>) result.geterror();
		ErrorDetail errorDetail = toErrorDetail(errorDetails);

		assertNotNull(errorDetail);
		assertEquals("User Error", errorDetail.getError());
		assertEquals("No blogs found for this user", errorDetail.getMessage());
	}

	@Test
	@Order(5)
	void testDeleteBlogByUserSuccess() {

		ApiResponse<Object, Object> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Blog deleted successfully",
				null);
		ResponseEntity<ApiResponse<Object, Object>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

		Mockito.when(restTemplate.exchange(Mockito.eq("http://service-blog/api/blogs/{id}/{userId}"),
				Mockito.eq(HttpMethod.DELETE), Mockito.isNull(), Mockito.any(Class.class), Mockito.eq(1L),
				Mockito.eq(1L))).thenReturn(responseEntity);

		ApiResponse<Object, Object> result = userBlogService.deteleBlogByUser(1L, 1L);

		assertNotNull(result);
		assertEquals(HttpStatus.OK.value(), result.getStatus());
		assertEquals("Blog deleted successfully", result.getData());
	}

	@Test
	@Order(2)
	void testGetBlogsByUserHttpClientErrorException() {

		String errorMessage = "{" + "\"status\": 400," + "\"data\": null," + "\"error\": {"
				+ "\"error\": \"User Error\"," + "\"message\": \"No blogs found for this user\","
				+ "\"timeStamp\": \"2024-10-15T17:12:46.8787456\"" + "},"
				+ "\"timeStamp\": \"2024-10-15T17:12:46.8787456\"" + "}";

		HttpClientErrorException httpClientErrorException = new HttpClientErrorException(HttpStatus.BAD_REQUEST,
				"Bad Request", errorMessage.getBytes(), null);

		Mockito.when(restTemplate.getForEntity(Mockito.eq("http://service-blog/api/blogs/get-blogs-by-id/{id}"),
				Mockito.any(Class.class), Mockito.eq(1L))).thenThrow(httpClientErrorException);

		ApiResponse<Object, Object> result = userBlogService.getBlogsByUser(1L);

		assertNotNull(result);

		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());

		assertNotNull(result.geterror());

		Map<String, Object> errorDetails = (Map<String, Object>) result.geterror();
		ErrorDetail errorDetail = toErrorDetail(errorDetails);

		assertNotNull(errorDetail);
		assertEquals("User Error", errorDetail.getError());
		assertEquals("No blogs found for this user", errorDetail.getMessage());
	}

	@Test
	@Order(3)
	void testGetBlogsByUserSuccess() {

		BlogRequestDTO blogRequestDTO2 = BlogRequestDTOBuilder.withAllDummy().setTitle("Another title")
				.setDescription("Description 2").setContent("Content 2").build2();

		List<BlogRequestDTO> blogs = new ArrayList<>();
		blogs.add(blogRequestDTO);
		blogs.add(blogRequestDTO2);

		ApiResponse<List<BlogRequestDTO>, Object> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), blogs, null);

		ResponseEntity<ApiResponse<List<BlogRequestDTO>, Object>> responseEntity = new ResponseEntity<>(apiResponse,
				HttpStatus.OK);

		Mockito.when(restTemplate.getForEntity(Mockito.eq("http://service-blog/api/blogs/get-blogs-by-id/{id}"),
				Mockito.any(Class.class), Mockito.eq(1L))).thenReturn(responseEntity);
		ApiResponse<List<BlogRequestDTO>, Object> result = userBlogService.getBlogsByUser(1L);

		assertNotNull(result);
		assertEquals(HttpStatus.OK.value(), result.getStatus());
		assertEquals(2, result.getData().size());
	}

	ErrorDetail toErrorDetail(Map<String, Object> mapError) {
		String error = (String) mapError.get("error");
		String message = (String) mapError.get("message");
		String timeStamp = (String) mapError.get("timeStamp");

		ErrorDetail errorDetail = new ErrorDetail(error, message,
				LocalDateTime.parse(timeStamp, DateTimeFormatter.ISO_DATE_TIME));

		return errorDetail;

	}

}
