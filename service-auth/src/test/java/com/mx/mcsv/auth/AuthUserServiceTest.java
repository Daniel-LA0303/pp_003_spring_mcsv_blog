package com.mx.mcsv.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mx.mcsv.auth.dto.ApiResponse;
import com.mx.mcsv.auth.dto.LoginUserDTO;
import com.mx.mcsv.auth.dto.TokenDto;
import com.mx.mcsv.auth.dto.UserDTO;
import com.mx.mcsv.auth.dto.UserResponseDTO;
import com.mx.mcsv.auth.exceptions.ErrorDetail;
import com.mx.mcsv.auth.security.JwtProvider;
import com.mx.mcsv.auth.service.AuthUserService;
import com.mx.mcsv.auth.utils.LoginUserDTOBuilder;
import com.mx.mcsv.auth.utils.TokenDtoBuilder;
import com.mx.mcsv.auth.utils.UserDTOBuilder;
import com.mx.mcsv.auth.utils.UserResponseDTOBuilder;

@SpringBootTest
public class AuthUserServiceTest {

	@InjectMocks
	private AuthUserService authUserService;

	@Mock
	private RestTemplate restTemplate;

	UserDTO userDTO;

	UserResponseDTO userResponseDTO;

	LoginUserDTO loginUserDTO;

	TokenDto tokenDto;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtProvider jwtProvider;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);

		userDTO = UserDTOBuilder.withAllDummy().build();
		loginUserDTO = LoginUserDTOBuilder.withAllDummy().build();
		tokenDto = TokenDtoBuilder.withDummyToken().build2();
		userResponseDTO = UserResponseDTOBuilder.withAllDummy().build();
	}

	@Test
	@Order(2)
	void testCreateUserHttpClientErrorException() throws Exception {

		String errorMessage = "{" + "\"status\": 400," + "\"data\": null," + "\"error\": {"
				+ "\"error\": \"User Error\"," + "\"message\": \"A error ocurred\","
				+ "\"timeStamp\": \"2024-10-15T17:12:46.8787456\"" + "},"
				+ "\"timeStamp\": \"2024-10-15T17:12:46.8787456\"" + "}";

		HttpClientErrorException httpClientErrorException = new HttpClientErrorException(HttpStatus.BAD_REQUEST,
				"Bad Request", errorMessage.getBytes(), null);

		Mockito.when(restTemplate.postForEntity(Mockito.eq("http://service-user/api/users/new-user"),
				Mockito.eq(userDTO), Mockito.any(Class.class))).thenThrow(httpClientErrorException);

		ApiResponse<Object, Object> result = authUserService.save(userDTO);

		assertNotNull(result);
		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
		assertNotNull(result.geterror());

		Map<String, Object> errorDetails = (Map<String, Object>) result.geterror();
		ErrorDetail errorDetail = toErrorDetail(errorDetails);

		assertNotNull(errorDetail);
		assertEquals("User Error", errorDetail.getError());
		assertEquals("A error ocurred", errorDetail.getMessage());
	}

	@Test
	@Order(1)
	void testCreateUserSuccess() throws Exception {

		String encodedPassword = "encodedPassword123";
		when(passwordEncoder.encode(userDTO.getPassword())).thenReturn(encodedPassword);

		ApiResponse<Object, Object> apiResponse = new ApiResponse<>(HttpStatus.CREATED.value(), userDTO, null);
		ResponseEntity<ApiResponse<Object, Object>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

		when(restTemplate.postForEntity(eq("http://service-user/api/users/new-user"), eq(userDTO), any(Class.class)))
				.thenReturn(responseEntity);

		ApiResponse<Object, Object> result = authUserService.save(userDTO);
		UserDTO userResponse = (UserDTO) result.getData();

		assertNotNull(result);
		assertEquals(HttpStatus.CREATED.value(), result.getStatus());
		assertTrue(result.getData() instanceof UserDTO);

		assertEquals(userDTO.getEmail(), userResponse.getEmail());
		assertEquals(userDTO.getName(), userResponse.getName());
		assertEquals(userDTO.getUsername(), userResponse.getUsername());

	}

	@Test
	@Order(5)
	void testLoginUserHttpClientErrorException() throws Exception {

		String errorMessage = "{" + "\"status\": 400," + "\"data\": null," + "\"error\": {"
				+ "\"error\": \"User Error\"," + "\"message\": \"A error ocurred\","
				+ "\"timeStamp\": \"2024-10-15T17:12:46.8787456\"" + "},"
				+ "\"timeStamp\": \"2024-10-15T17:12:46.8787456\"" + "}";

		HttpClientErrorException httpClientErrorException = new HttpClientErrorException(HttpStatus.BAD_REQUEST,
				"Bad Request", errorMessage.getBytes(), null);

		loginUserDTO.setPassword("1234");
		String simulatedResponse = "{id=1, name=Luis, username=luis10.da, email=email10@email.com, password=$2a$10$iyWZlq0.U5xPA8IvSzhPLeo0Lu4v6I9ntvO.xZLJ8mgTCKd5Vbotu}";
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

		when(restTemplate.getForEntity(eq("http://service-user/api/users/get-by-email/{email}"), any(Class.class),
				eq(loginUserDTO.getEmail()))).thenThrow(httpClientErrorException);

		ApiResponse<Object, Object> result = authUserService.login(loginUserDTO);

		assertNotNull(result);
		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());

		Map<String, Object> errorDetails = (Map<String, Object>) result.geterror();
		ErrorDetail errorDetail = toErrorDetail(errorDetails);

		assertNotNull(errorDetail);
		assertEquals("User Error", errorDetail.getError());
		assertEquals("A error ocurred", errorDetail.getMessage());
	}

	@Test
	@Order(4)
	void testLoginUserIncorrectPassword() throws Exception {

		String simulatedResponse = "{id=1, name=Luis, username=luis10.da, email=email10@email.com, password=$2a$10$iyWZlq0.U5xPA8IvSzhPLeo0Lu4v6I9ntvO.xZLJ8mgTCKd5Vbotu}";

		ApiResponse<Object, Object> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), simulatedResponse, null);
		ResponseEntity<ApiResponse<Object, Object>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

		ApiResponse<Object, Object> incorrectPasswordResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
				"Incorrect password", null);

		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

		when(restTemplate.getForEntity(eq("http://service-user/api/users/get-by-email/{email}"), any(Class.class),
				eq(loginUserDTO.getEmail()))).thenReturn(responseEntity);

		ApiResponse<Object, Object> response = authUserService.login(loginUserDTO);

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

	}

	@Test
	@Order(3)
	void testLoginUserSuccess() throws Exception {

		loginUserDTO.setPassword("1234");
		String simulatedResponse = "{id=1, name=Luis, username=luis10.da, email=email10@email.com, password=$2a$10$iyWZlq0.U5xPA8IvSzhPLeo0Lu4v6I9ntvO.xZLJ8mgTCKd5Vbotu}";

		ApiResponse<String, Object> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), simulatedResponse, null);
		ResponseEntity<ApiResponse<String, Object>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

		String simulatedToken = "simulated_jwt_token";
		doReturn(simulatedToken).when(jwtProvider).createToken(any());

		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

		when(restTemplate.getForEntity(eq("http://service-user/api/users/get-by-email/{email}"), any(Class.class),
				eq(loginUserDTO.getEmail()))).thenReturn(responseEntity);

		ApiResponse<Object, Object> result = authUserService.login(loginUserDTO);

		assertNotNull(result);
		assertEquals(HttpStatus.OK.value(), result.getStatus());
		assertTrue(result.getData() instanceof TokenDto);

	}

	@Test
	@Order(5)
	void testValidateEmailMismatch() throws Exception {
		String simulatedResponse = "{id=1, name=Luis, username=luis10.da, email=email20@email.com, password=$2a$10$iyWZlq0.U5xPA8IvSzhPLeo0Lu4v6I9ntvO.xZLJ8mgTCKd5Vbotu}";
		ApiResponse<String, Object> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), simulatedResponse, null);
		ResponseEntity<ApiResponse<String, Object>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

		String simulatedToken = "simulated_jwt_token";

		when(jwtProvider.validate(simulatedToken)).thenReturn(true);
		when(jwtProvider.getEmailFromToken(simulatedToken)).thenReturn("email10@email.com");
		when(restTemplate.getForEntity(eq("http://service-user/api/users/get-by-email/{email}"), any(Class.class),
				eq("email10@email.com"))).thenReturn(responseEntity);

		TokenDto result = authUserService.validate(simulatedToken);

		assertNull(result);
		verify(restTemplate).getForEntity(eq("http://service-user/api/users/get-by-email/{email}"), any(Class.class),
				eq("email10@email.com"));
	}

	@Test
	@Order(3)
	void testValidateSuccess() throws Exception {

		String simulatedResponse = "{id=1, name=Luis, username=luis10.da, email=email10@email.com, password=$2a$10$iyWZlq0.U5xPA8IvSzhPLeo0Lu4v6I9ntvO.xZLJ8mgTCKd5Vbotu}";
		ApiResponse<String, Object> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), simulatedResponse, null);
		ResponseEntity<ApiResponse<String, Object>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

		String simulatedToken = "simulated_jwt_token";
		doReturn(simulatedToken).when(jwtProvider).createToken(any());

		when(jwtProvider.validate(simulatedToken)).thenReturn(true);
		when(jwtProvider.getEmailFromToken(simulatedToken)).thenReturn("email10@email.com");
		when(restTemplate.getForEntity(eq("http://service-user/api/users/get-by-email/{email}"), any(Class.class),
				eq("email10@email.com"))).thenReturn(responseEntity);
		TokenDto result = authUserService.validate(simulatedToken);
		assertNotNull(result);

	}

	@Test
	@Order(4)
	void testValidateTokenInvalid() throws Exception {
		String invalidToken = "invalid_jwt_token";

		when(jwtProvider.validate(invalidToken)).thenReturn(false);
		TokenDto result = authUserService.validate(invalidToken);
		assertNull(result);
		verify(jwtProvider).validate(invalidToken);
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
