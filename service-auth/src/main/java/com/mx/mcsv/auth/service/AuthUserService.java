package com.mx.mcsv.auth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mx.mcsv.auth.dto.AuthUserDto;
import com.mx.mcsv.auth.dto.TokenDto;
import com.mx.mcsv.auth.entity.AuthUser;
import com.mx.mcsv.auth.exceptions.AuthException;
import com.mx.mcsv.auth.repository.AuthUserRepository;
import com.mx.mcsv.auth.security.JwtProvider;

@Service
public class AuthUserService {

	@Autowired
	AuthUserRepository authUserRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtProvider jwtProvider;

	public TokenDto login(AuthUserDto dto) throws AuthException {

		Optional<AuthUser> user = authUserRepository.findByUserName(dto.getUserName());
		if (!user.isPresent()) {
			throw new AuthException("Username not found", HttpStatus.NOT_FOUND);
		}

		if (!passwordEncoder.matches(dto.getPassword(), user.get().getPassword())) {
			throw new AuthException("Password incorrect", HttpStatus.BAD_REQUEST);
		}
		return new TokenDto(jwtProvider.createToken(user.get()));
	}

	public AuthUser save(AuthUserDto dto) throws AuthException {
		Optional<AuthUser> user = authUserRepository.findByUserName(dto.getUserName());
		if (user.isPresent()) {
			throw new AuthException("Username already in use by another user", HttpStatus.BAD_REQUEST);
		}
		String password = passwordEncoder.encode(dto.getPassword());
		AuthUser authUser = AuthUser.builder().userName(dto.getUserName()).password(password).build();
		return authUserRepository.save(authUser);
	}

	public TokenDto validate(String token) {
		if (!jwtProvider.validate(token)) {
			return null;
		}
		String username = jwtProvider.getUserNameFromToken(token);
		if (!authUserRepository.findByUserName(username).isPresent()) {
			return null;
		}
		return new TokenDto(token);
	}
}