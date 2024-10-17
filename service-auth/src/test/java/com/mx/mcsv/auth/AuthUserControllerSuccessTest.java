package com.mx.mcsv.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.mx.mcsv.auth.controller.AuthUserController;
import com.mx.mcsv.auth.dto.ApiResponse;
import com.mx.mcsv.auth.dto.LoginUserDTO;
import com.mx.mcsv.auth.dto.TokenDto;
import com.mx.mcsv.auth.dto.UserDTO;
import com.mx.mcsv.auth.service.AuthUserService;
import com.mx.mcsv.auth.utils.LoginUserDTOBuilder;
import com.mx.mcsv.auth.utils.TokenDtoBuilder;
import com.mx.mcsv.auth.utils.UserDTOBuilder;

@ExtendWith(MockitoExtension.class)
public class AuthUserControllerSuccessTest {

	@InjectMocks
	private AuthUserController authUserController;

	@Mock
	private AuthUserService authUserService;

	UserDTO userDTO;

	LoginUserDTO loginUserDTO;

	TokenDto tokenDto;

	@Test
	public void testCreateUserSuccess() throws Exception {

		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);

		ApiResponse<Object, Object> apiResponse = new ApiResponse<>(HttpStatus.CREATED.value(), userDTO, null);

		Mockito.when(authUserService.save(userDTO)).thenReturn(apiResponse);

		ResponseEntity<ApiResponse<Object, Object>> responseEntity = (ResponseEntity<ApiResponse<Object, Object>>) authUserController
				.create(userDTO, bindingResult);

		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(apiResponse, responseEntity.getBody());
		assertEquals(null, apiResponse.geterror());

	}

	@Test
	public void testLoginUserSuccess() throws Exception {

		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);

		TokenDto tokenDto = TokenDtoBuilder.withDummyToken().build();

		ApiResponse<Object, Object> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), tokenDto, null);

		Mockito.when(authUserService.login(loginUserDTO)).thenReturn(apiResponse);

		ResponseEntity<ApiResponse<TokenDto, Object>> responseEntity = (ResponseEntity<ApiResponse<TokenDto, Object>>) authUserController
				.login(loginUserDTO, bindingResult);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(apiResponse, responseEntity.getBody());
		assertEquals(null, apiResponse.geterror());
	}

	@Test
	public void testValidateTokenFailure() throws Exception {
		String invalidToken = "invalid_jwt_token";

		Mockito.when(authUserService.validate(invalidToken)).thenReturn(null);

		ResponseEntity<TokenDto> responseEntity = authUserController.validate(invalidToken);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());
	}

	@Test
	public void testValidateTokenSuccess() throws Exception {
		String validToken = "valid_jwt_token";

		TokenDto tokenDto = new TokenDto(validToken);
		Mockito.when(authUserService.validate(validToken)).thenReturn(tokenDto);
		ResponseEntity<TokenDto> responseEntity = authUserController.validate(validToken);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(tokenDto, responseEntity.getBody());
	}

	@BeforeEach
	void setUp() {
		userDTO = UserDTOBuilder.withAllDummy().build();
		loginUserDTO = LoginUserDTOBuilder.withAllDummy().build();
		tokenDto = TokenDtoBuilder.withDummyToken().build();
	}
}
