package com.mx.mcsv.user.services.communication.blog;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mx.mcsv.user.controller.UserBlogController;
import com.mx.mcsv.user.dto.ApiResponse;
import com.mx.mcsv.user.dto.BlogRequestDTO;
import com.mx.mcsv.user.dto.UserDTO;
import com.mx.mcsv.user.service.UserService;
import com.mx.mcsv.user.service.impl.UserBlogServiceImpl;
import com.mx.mcsv.user.utils.BlogRequestDTOBuilder;

@ExtendWith(MockitoExtension.class)
public class UserBlogControllerSuccessTest {

	@InjectMocks
	private UserBlogController userBlogController;

	@Mock
	private UserService userService;

	@Mock
	private UserBlogServiceImpl userBlogService;

	BlogRequestDTO blogRequestDTO;

	@Test
	public void testCreateBlogForUserSuccess() throws Exception {

		ApiResponse<Object, Object> apiResponse = new ApiResponse<>(HttpStatus.CREATED.value(), blogRequestDTO, null);

		Mockito.when(userService.findById(blogRequestDTO.getUserId())).thenReturn(new UserDTO());
		Mockito.when(userBlogService.createBlog(blogRequestDTO)).thenReturn(apiResponse);

		ResponseEntity<ApiResponse<BlogRequestDTO, String>> responseEntity = (ResponseEntity<ApiResponse<BlogRequestDTO, String>>) userBlogController
				.createBlogForUser(blogRequestDTO);

		System.out.println("*******" + responseEntity.getStatusCode());
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(apiResponse, responseEntity.getBody());
		assertEquals(null, apiResponse.geterror());

		BlogRequestDTO blogResponse = responseEntity.getBody().getData();
		assertEquals(blogRequestDTO.getContent(), blogResponse.getContent());
		assertEquals(blogRequestDTO.getDescription(), blogResponse.getDescription());
		assertEquals(blogRequestDTO.getStatus(), blogResponse.getStatus());
		assertEquals(blogRequestDTO.getTitle(), blogResponse.getTitle());
		assertEquals(blogRequestDTO.getUserId(), blogResponse.getUserId());

		Mockito.verify(userService).findById(blogRequestDTO.getUserId());
		Mockito.verify(userBlogService).createBlog(blogRequestDTO);
	}

	@Test
	public void testDeleteBlogForUserSuccess() throws Exception {

		ApiResponse<Object, Object> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Blog deleted", null);

		Mockito.when(userService.findById(1L)).thenReturn(new UserDTO());
		Mockito.when(userBlogService.deteleBlogByUser(1L, 1L)).thenReturn(apiResponse);

		ResponseEntity<ApiResponse<String, String>> responseEntity = (ResponseEntity<ApiResponse<String, String>>) userBlogController
				.deteleBlog(1L, 1L);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(apiResponse, responseEntity.getBody());
		assertEquals("Blog deleted", responseEntity.getBody().getData());
		assertEquals(null, responseEntity.getBody().geterror());

		Mockito.verify(userService).findById(1L);
		Mockito.verify(userBlogService).deteleBlogByUser(1L, 1L);
	}

	@Test
	public void testGetBlogsByUserSuccess() throws Exception {
		BlogRequestDTO blogRequestDTO2 = BlogRequestDTOBuilder.withAllDummy().setTitle("Another title")
				.setDescription("Description 2").setContent("Content 2").build2();

		List<BlogRequestDTO> blogs = new ArrayList<>();
		blogs.add(blogRequestDTO);
		blogs.add(blogRequestDTO2);

		ApiResponse<Object, Object> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), blogs, null);
		apiResponse.setStatus(200);

		Mockito.when(userService.findById(1L)).thenReturn(new UserDTO());
		Mockito.when(userBlogService.getBlogsByUser(1L)).thenReturn(apiResponse);

		ResponseEntity<ApiResponse<List<BlogRequestDTO>, String>> responseEntity = (ResponseEntity<ApiResponse<List<BlogRequestDTO>, String>>) userBlogController
				.getBlogsByUser(1L);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(apiResponse, responseEntity.getBody());
		assertEquals(blogs, responseEntity.getBody().getData());
		assertEquals(2, responseEntity.getBody().getData().size());
		assertEquals(null, responseEntity.getBody().geterror());

		Mockito.verify(userService).findById(1L);
		Mockito.verify(userBlogService).getBlogsByUser(1L);
	}

	@BeforeEach
	void setUp() {

		blogRequestDTO = BlogRequestDTOBuilder.withAllDummy().build();

	}

}
