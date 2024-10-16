package com.mx.mcsv.user.services;

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

import com.mx.mcsv.user.dto.ApiResponse;
import com.mx.mcsv.user.dto.UserDTO;
import com.mx.mcsv.user.entity.User;
import com.mx.mcsv.user.exceptions.ErrorDetail;
import com.mx.mcsv.user.utils.UserBuilder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/import.sql")
@ActiveProfiles("test")
public class UserServiceTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@LocalServerPort
	private int port;

	HttpHeaders headers;

	User userTest;

	User validUser;

	User validUser2;

	@Test
	@Order(10)
	void deleteCommentTest() {

		ParameterizedTypeReference<ApiResponse<String, String>> responseType = new ParameterizedTypeReference<ApiResponse<String, String>>() {
		};

		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<ApiResponse<String, String>> response = testRestTemplate.exchange("/api/users/{id}",
				HttpMethod.DELETE, requestEntity, responseType, 1L);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		ApiResponse<String, String> apiResponse = response.getBody();
		assertNotNull(apiResponse);
		System.out.println("****** dele");
		System.out.println(apiResponse.geterror());
		assertEquals(200, apiResponse.getStatus());
		assertEquals("User deleted", apiResponse.getData());
		assertNull(apiResponse.geterror());

		assertNotNull(apiResponse.getTimeStamp());

	}

	@Test
	@Order(1)
	void getAllUsersTest() {
		ResponseEntity<ApiResponse> response = testRestTemplate.getForEntity("/api/users", ApiResponse.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		ApiResponse<List<UserDTO>, String> apiResponse = response.getBody();
		assertNotNull(apiResponse);
		assertEquals(200, apiResponse.getStatus());
		assertNull(apiResponse.geterror());

		List<UserDTO> users = apiResponse.getData();
		assertNotNull(users);
		assertNotNull(apiResponse.getTimeStamp());
	}

	@Test
	@Order(4)
	void getEmailTest() {
		String email = "email@email.com";

		ParameterizedTypeReference<ApiResponse<User, String>> responseType = new ParameterizedTypeReference<ApiResponse<User, String>>() {
		};

		ResponseEntity<ApiResponse<User, String>> response = testRestTemplate
				.exchange("/api/users/get-by-email/" + email, HttpMethod.GET, null, responseType);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		ApiResponse<User, String> apiResponse = response.getBody();

		assertNotNull(apiResponse);

		assertEquals(200, apiResponse.getStatus());

		assertNull(apiResponse.geterror());

		User user = apiResponse.getData();

		assertNotNull(user);
		assertEquals(email, user.getEmail());

		assertNotNull(apiResponse.getTimeStamp());
	}

	@Test
	@Order(2)
	void getUserTest() {
		ParameterizedTypeReference<ApiResponse<UserDTO, String>> responseType = new ParameterizedTypeReference<ApiResponse<UserDTO, String>>() {
		};

		ResponseEntity<ApiResponse<UserDTO, String>> response = testRestTemplate.exchange("/api/users/1",
				HttpMethod.GET, null, responseType);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		ApiResponse<UserDTO, String> apiResponse = response.getBody();
		assertNotNull(apiResponse);
		assertEquals(200, apiResponse.getStatus());
		assertNull(apiResponse.geterror());

		UserDTO userDTO = apiResponse.getData();

		assertEquals(userDTO.getId(), userTest.getId());
		assertEquals(userDTO.getName(), userTest.getName());
		assertEquals(userDTO.getUsername(), userTest.getUsername());
		assertEquals(userDTO.getEmail(), userTest.getEmail());

		assertNotNull(apiResponse.getTimeStamp());
	}

	@Test
	@Order(3)
	void saveUserTest() {

		HttpEntity<User> requestEntity = new HttpEntity<>(validUser, headers);

		ResponseEntity<ApiResponse<UserDTO, String>> response = testRestTemplate.exchange("/api/users/new-user",
				HttpMethod.POST, requestEntity, new ParameterizedTypeReference<ApiResponse<UserDTO, String>>() {
				});

		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		ApiResponse<UserDTO, String> apiResponse = response.getBody();
		assertNotNull(apiResponse);
		assertEquals(201, apiResponse.getStatus());
		assertNull(apiResponse.geterror());

		UserDTO userDTO = new UserDTO(apiResponse.getData().getId(), apiResponse.getData().getName(),
				apiResponse.getData().getUsername(), apiResponse.getData().getEmail());

		assertNotNull(userDTO);
		assertEquals(userDTO.getName(), validUser.getName());
		assertEquals(userDTO.getEmail(), validUser.getEmail());
		assertEquals(userDTO.getUsername(), validUser.getUsername());

		assertNotNull(apiResponse.getTimeStamp());
	}

	@BeforeEach
	void setUp() {

		userTest = UserBuilder.withAllDummy().build();

		validUser = UserBuilder.withAllDummy().setId(null).setEmail("email@email.com").setName("new.user")
				.setPassword("password").setUsername("new.user").build();

		validUser2 = UserBuilder.withAllDummy().setId(1L).setEmail("john@example.com").setName("johndoe.edit")
				.setPassword("password").setUsername("johndoe.edit").build();

		headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

	}

	@Test
	@Order(6)
	void testSaveUserDuplicateEmail() {

		HttpEntity<User> requestEntity = new HttpEntity<>(validUser, headers);

		ResponseEntity<ApiResponse<String, ErrorDetail>> response = testRestTemplate.exchange("/api/users/new-user",
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
		assertEquals("User already exists with this email", errorMessage.getMessage());

		assertNotNull(apiResponse.getTimeStamp());
	}

	@Test
	@Order(7)
	void testSaveUserDuplicateUsername() {

		validUser = UserBuilder.withAllDummy().setId(null).setEmail("email100@email.com").setName("new.user")
				.setPassword("password").setUsername("new.user").build();

		HttpEntity<User> requestEntity = new HttpEntity<>(validUser, headers);

		ResponseEntity<ApiResponse<String, ErrorDetail>> response = testRestTemplate.exchange("/api/users/new-user",
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
		assertEquals("Username already in use by another user", errorMessage.getMessage());

		assertNotNull(apiResponse.getTimeStamp());
	}

	@Test
	@Order(8)
	void testUpdateUserDuplicateEmail() {

		validUser = UserBuilder.withAllDummy().setId(null).setEmail("email100@email.com").setName("new.user")
				.setPassword("password").setUsername("new.user").build();

		HttpEntity<User> requestEntity = new HttpEntity<>(validUser, headers);

		ResponseEntity<ApiResponse<String, ErrorDetail>> response = testRestTemplate.exchange("/api/users/1",
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
		assertEquals("Username already in use by another user", errorMessage.getMessage());

		assertNotNull(apiResponse.getTimeStamp());
	}

	@Test
	@Order(9)
	void testUpdateUserDuplicateUsername() {

		HttpEntity<User> requestEntity = new HttpEntity<>(validUser, headers);

		ResponseEntity<ApiResponse<String, ErrorDetail>> response = testRestTemplate.exchange("/api/users/1",
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
		assertEquals("Email already in use by another user", errorMessage.getMessage());

		assertNotNull(apiResponse.getTimeStamp());
	}

	@Test
	@Order(5)
	void updateUserTest() {

		HttpEntity<User> requestEntity = new HttpEntity<>(validUser2, headers);
		ResponseEntity<ApiResponse<UserDTO, String>> response = testRestTemplate.exchange("/api/users/1",
				HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<ApiResponse<UserDTO, String>>() {
				});

		assertEquals(HttpStatus.OK, response.getStatusCode());

		ApiResponse<UserDTO, String> apiResponse = response.getBody();
		assertNotNull(apiResponse);
		assertEquals(200, apiResponse.getStatus());
		assertNull(apiResponse.geterror());

		UserDTO userDTO = apiResponse.getData();

		assertNotNull(userDTO);
		assertEquals(userDTO.getName(), validUser2.getName());
		assertEquals(userDTO.getEmail(), validUser2.getEmail());
		assertEquals(userDTO.getUsername(), validUser2.getUsername());
		assertNotNull(apiResponse.getTimeStamp());
	}

}
