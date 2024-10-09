package com.mx.mcsv.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.mx.mcsv.user.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.email = :email")
	Optional<User> findByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.username = ?1")
	Optional<User> findByUsername(String username);

}
