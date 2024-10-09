package com.mx.mcsv.user.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mx.mcsv.user.dto.UserDTO;
import com.mx.mcsv.user.entity.User;
import com.mx.mcsv.user.exceptions.UserException;
import com.mx.mcsv.user.repository.UserRepository;
import com.mx.mcsv.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public void deleteUser(Long id) throws UserException {

		Optional<User> user = userRepository.findById(id);

		if (user.isPresent()) {
			userRepository.deleteById(user.get().getId());
			return;
		}

		throw new UserException("User not found with id: " + id, HttpStatus.NOT_FOUND);
	}

	@Override
	public List<UserDTO> findAll() {

		List<User> users = (List<User>) userRepository.findAll();

		return users.stream().map(this::convertToUserDTO).collect(Collectors.toList());
	}

	@Override
	public UserDTO findById(Long id) throws UserException {

		Optional<User> user = userRepository.findById(id);

		if (user.isPresent()) {
			return convertToUserDTO(user.get());
		}

		throw new UserException("User not found with id: " + id, HttpStatus.NOT_FOUND);
	}

	@Override
	public UserDTO save(User user) throws UserException {

		Optional<User> userDB = userRepository.findByEmail(user.getEmail());
		if (userDB.isPresent()) {
			throw new UserException("User already exists with this email", HttpStatus.BAD_REQUEST);
		}

		Optional<User> userByUsername = userRepository.findByUsername(user.getUsername());
		if (userByUsername.isPresent()) {
			throw new UserException("Username already in use by another user", HttpStatus.BAD_REQUEST);
		}

		User savedUser = userRepository.save(user);
		return convertToUserDTO(savedUser);
	}

	@Override
	public UserDTO update(Long id, User user) throws UserException {
		Optional<User> userO = userRepository.findById(id);

		if (userO.isPresent()) {
			User existingUser = userO.get();

			if (!existingUser.getEmail().equals(user.getEmail())) {
				Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());
				if (userByEmail.isPresent()) {
					throw new UserException("Email already in use by another user", HttpStatus.BAD_REQUEST);
				}
			}

			if (!existingUser.getUsername().equals(user.getUsername())) {
				Optional<User> userByUsername = userRepository.findByUsername(user.getUsername());
				if (userByUsername.isPresent()) {
					throw new UserException("Username already in use by another user", HttpStatus.BAD_REQUEST);
				}
			}

			existingUser.setName(user.getName());
			existingUser.setUsername(user.getUsername());
			existingUser.setEmail(user.getEmail());
			existingUser.setPassword(user.getPassword());

			userRepository.save(existingUser);

			return convertToUserDTO(existingUser);
		}

		throw new UserException("User not found with id: " + id, HttpStatus.NOT_FOUND);
	}

	private UserDTO convertToUserDTO(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setName(user.getName());
		userDTO.setUsername(user.getUsername());
		userDTO.setEmail(user.getEmail());
		return userDTO;
	}

}
