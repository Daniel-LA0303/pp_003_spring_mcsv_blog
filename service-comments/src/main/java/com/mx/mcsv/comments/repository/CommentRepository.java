package com.mx.mcsv.comments.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.mx.mcsv.comments.entity.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {

	@Query("SELECT c from Comment c where c.blogId = :id")
	List<Comment> getCommentsByBlog(Long id);

	@Query("SELECT c from Comment c where c.userId = :id")
	List<Comment> getCommentsByUser(Long id);

}
