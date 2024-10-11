package com.mx.mcsv.user.service;

import java.util.List;

import com.mx.mcsv.user.dto.UserDTO;
import com.mx.mcsv.user.entity.User;
import com.mx.mcsv.user.exceptions.UserException;

public interface UserService {

	void deleteUser(Long id) throws UserException;

	List<UserDTO> findAll();

	UserDTO findById(Long id) throws UserException;

	User getUserByEmail(String email) throws UserException;

	UserDTO save(User user) throws UserException;

	UserDTO update(Long id, User user) throws UserException;

}
