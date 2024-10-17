package com.mx.mcsv.auth;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mx.mcsv.auth.dto.LoginUserDTO;
import com.mx.mcsv.auth.dto.UserDTO;
import com.mx.mcsv.auth.service.AuthUserService;
import com.mx.mcsv.auth.utils.LoginUserDTOBuilder;
import com.mx.mcsv.auth.utils.UserDTOBuilder;

@SpringBootTest
@AutoConfigureMockMvc
public class AtuhUserControllerCircuitBreakerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthUserService authUserService;

	// @MockBean
	// private UserBlogServiceImpl userBlogService;

	UserDTO userDTO;

	UserDTO invalidUserDTO;

	LoginUserDTO loginUserDTO;

	LoginUserDTO invalidLoginUserDTO;

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@BeforeEach
	void setUp() {
		userDTO = UserDTOBuilder.withAllDummy().build();
		loginUserDTO = LoginUserDTOBuilder.withAllDummy().build();
		invalidUserDTO = UserDTOBuilder.withAllDummy().setEmail("").setName("").setPassword("").setUsername("")
				.build2();
		invalidLoginUserDTO = LoginUserDTOBuilder.withAllDummy().setEmail("").setPassword("").build2();
	}

	@Test
	void testCreateUserCircuiBreaker() throws Exception {

		mockMvc.perform(post("/auth/create").contentType(MediaType.APPLICATION_JSON).content(asJsonString(userDTO)))
				.andExpect(status().isServiceUnavailable())
				.andExpect(jsonPath("$.status").value(HttpStatus.SERVICE_UNAVAILABLE.value()))
				.andExpect(jsonPath("$.data").value(nullValue()))
				.andExpect(jsonPath("$.error").value("The User could not be created at this time due to an error."));

	}

	@Test
	void testCreateUserValidationErrors() throws Exception {

		mockMvc.perform(
				post("/auth/create").contentType(MediaType.APPLICATION_JSON).content(asJsonString(invalidUserDTO))) // validación
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error.password").value("The field password must not be null"))
				.andExpect(jsonPath("$.error.name").value("The field name must not be blank"))
				.andExpect(jsonPath("$.error.email").value("The field email must not be blank"))
				.andExpect(jsonPath("$.error.username").value("The field username must not be blank"))
				.andExpect(jsonPath("$.data").isEmpty()).andExpect(jsonPath("$.timeStamp").exists());
	}

	@Test
	void testLoginUserCircuiBreaker() throws Exception {

		mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(loginUserDTO)))
				.andExpect(status().isServiceUnavailable())
				.andExpect(jsonPath("$.status").value(HttpStatus.SERVICE_UNAVAILABLE.value()))
				.andExpect(jsonPath("$.data").value(nullValue()))
				.andExpect(jsonPath("$.error").value("The User could not be login at this time."));

	}

	@Test
	void testLoginUserValidationErrors() throws Exception {

		mockMvc.perform(
				post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(invalidLoginUserDTO)))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error.password").value("The field password must not be null"))
				.andExpect(jsonPath("$.error.email").value("The field email must not be blank"))
				.andExpect(jsonPath("$.data").isEmpty()).andExpect(jsonPath("$.timeStamp").exists());
	}

}
