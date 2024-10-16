package com.mx.mcsv.user.services.communication.comment;

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
import com.mx.mcsv.user.dto.CommentRequestDTO;
import com.mx.mcsv.user.dto.UserDTO;
import com.mx.mcsv.user.service.UserService;
import com.mx.mcsv.user.service.impl.UserCommentServiceImpl;
import com.mx.mcsv.user.utils.CommentRequestDTOBuilder;

@SpringBootTest
@AutoConfigureMockMvc
public class UserCommentControllerCircuitBreakerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private UserCommentServiceImpl userCommentService;

	CommentRequestDTO commentRequestDTO;

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@BeforeEach
	void setUp() {
		commentRequestDTO = CommentRequestDTOBuilder.withAllDummy().build();
	}

	@Test
	void testCreateCommentForUserCircuiBreaker() throws Exception {

		Mockito.when(userService.findById(1L)).thenReturn(new UserDTO());

		mockMvc.perform(post("/api/users/comment").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(commentRequestDTO))).andExpect(status().isServiceUnavailable())
				.andExpect(jsonPath("$.status").value(HttpStatus.SERVICE_UNAVAILABLE.value()))
				.andExpect(jsonPath("$.data").value(nullValue()))
				.andExpect(jsonPath("$.error").value("Unable to create comment at the moment, please try again later"));

		Mockito.verify(userService).findById(1L);
	}

	@Test
	void testDeleteCommentForUserCircuiBreaker() throws Exception {

		Mockito.when(userService.findById(1L)).thenReturn(new UserDTO());

		mockMvc.perform(delete("/api/users/comment/delete-comment/1/1/1").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(commentRequestDTO))).andExpect(status().isServiceUnavailable())
				.andExpect(jsonPath("$.status").value(HttpStatus.SERVICE_UNAVAILABLE.value()))
				.andExpect(jsonPath("$.data").value(nullValue()))
				.andExpect(jsonPath("$.error").value("Unable to delete comment at the moment."));

		Mockito.verify(userService).findById(1L);
	}

	@Test
	void testGetCommentByBlogCircuiBreaker() throws Exception {

		Mockito.when(userService.findById(1L)).thenReturn(new UserDTO());

		mockMvc.perform(get("/api/users/comment/by-blog/1").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(commentRequestDTO))).andExpect(status().isServiceUnavailable())
				.andExpect(jsonPath("$.status").value(HttpStatus.SERVICE_UNAVAILABLE.value()))
				.andExpect(jsonPath("$.data").value(nullValue()))
				.andExpect(jsonPath("$.error").value("Unable to retrieve comments for this blog at the moment."));

	}

	@Test
	void testGetCommentByUserCircuiBreaker() throws Exception {

		Mockito.when(userService.findById(1L)).thenReturn(new UserDTO());

		mockMvc.perform(get("/api/users/comment/by-user/1").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(commentRequestDTO))).andExpect(status().isServiceUnavailable())
				.andExpect(jsonPath("$.status").value(HttpStatus.SERVICE_UNAVAILABLE.value()))
				.andExpect(jsonPath("$.data").value(nullValue()))
				.andExpect(jsonPath("$.error").value("Unable to retrieve comments for this user at the moment."));

		Mockito.verify(userService).findById(1L);
	}

}
