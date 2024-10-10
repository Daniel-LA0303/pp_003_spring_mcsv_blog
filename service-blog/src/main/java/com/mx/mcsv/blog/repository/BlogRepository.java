package com.mx.mcsv.blog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.mx.mcsv.blog.entity.Blog;

public interface BlogRepository extends CrudRepository<Blog, Long> {

	@Query("SELECT b from Blog b where b.userId = :id")
	List<Blog> findBlogsByUserId(Long id);

	@Query("SELECT b FROM Blog b WHERE b.title = :title")
	Optional<Blog> findByTitle(String title);

}
