package com.mx.mcsv.user.services.communication.blog;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mx.mcsv.user.dto.BlogRequestDTO;
import com.mx.mcsv.user.dto.UserDTO;
import com.mx.mcsv.user.service.UserService;
import com.mx.mcsv.user.service.impl.UserBlogServiceImpl;
import com.mx.mcsv.user.utils.BlogRequestDTOBuilder;

@SpringBootTest
@AutoConfigureMockMvc
public class UserBlogControllerCircuitBreakerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private UserBlogServiceImpl userBlogService;

	BlogRequestDTO blogRequestDTO;

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@BeforeEach
	void setUp() {
		blogRequestDTO = BlogRequestDTOBuilder.withAllDummy().build();
	}

	@Test
	void testCreateBlogForUserCircuiBreaker() throws Exception {

		Mockito.when(userService.findById(1L)).thenReturn(new UserDTO());

		mockMvc.perform(
				post("/api/users/blog").contentType(MediaType.APPLICATION_JSON).content(asJsonString(blogRequestDTO)))
				.andExpect(status().isServiceUnavailable())
				.andExpect(jsonPath("$.status").value(HttpStatus.SERVICE_UNAVAILABLE.value()))
				.andExpect(jsonPath("$.data").value(nullValue()))
				.andExpect(jsonPath("$.error").value("The blog could not be created at this time."));

		Mockito.verify(userService).findById(1L);
	}

	@Test
	void testDeleteBlogForUserCircuiBreaker() throws Exception {

		Mockito.when(userService.findById(1L)).thenReturn(new UserDTO());

		mockMvc.perform(delete("/api/users/blog/1/1").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(blogRequestDTO))).andExpect(status().isServiceUnavailable())
				.andExpect(jsonPath("$.status").value(HttpStatus.SERVICE_UNAVAILABLE.value()))
				.andExpect(jsonPath("$.data").value(nullValue()))
				.andExpect(jsonPath("$.error").value("The blog could not be deleted at this time."));

		Mockito.verify(userService).findById(1L);
	}

	@Test
	void testGetBlogByUserCircuiBreaker() throws Exception {

		Mockito.when(userService.findById(1L)).thenReturn(new UserDTO());

		mockMvc.perform(
				get("/api/users/blog/1").contentType(MediaType.APPLICATION_JSON).content(asJsonString(blogRequestDTO)))
				.andExpect(status().isServiceUnavailable())
				.andExpect(jsonPath("$.status").value(HttpStatus.SERVICE_UNAVAILABLE.value()))
				.andExpect(jsonPath("$.data").value(nullValue()))
				.andExpect(jsonPath("$.error").value("Unable to obtain user blogs at this time."));

		Mockito.verify(userService).findById(1L);
	}
}
