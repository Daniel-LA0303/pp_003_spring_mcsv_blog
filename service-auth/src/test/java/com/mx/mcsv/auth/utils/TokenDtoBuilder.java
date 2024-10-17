package com.mx.mcsv.auth.utils;

import com.mx.mcsv.auth.dto.TokenDto;

public class TokenDtoBuilder {

	/**
	 * Token
	 */
	private String token;

	public static TokenDtoBuilder withDummyToken() {
		return new TokenDtoBuilder().setToken(
				"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InJvbXNxdHJwIiwiZW1haWwiOiJyb21zcXRycEBleGFtcGxlLmNvbSIsImV4cCI6MTcyOTEwNzYyMH0.xWHVScUGqUfGnkUTYLN7dALrnVMEr1yuVmW0PC41_oc");
	}

	public TokenDto build() {
		TokenDto tokenDto = new TokenDto();
		tokenDto.setToken(token);
		return tokenDto;
	}

	public TokenDto build2() {
		return new TokenDto(token);
	}

	/**
	 * Set the value of the property token
	 *
	 * @param token the token to set
	 */
	public TokenDtoBuilder setToken(String token) {
		this.token = token;
		return this;
	}
}
