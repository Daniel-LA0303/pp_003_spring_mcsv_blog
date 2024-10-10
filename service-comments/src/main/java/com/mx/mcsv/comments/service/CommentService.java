package com.mx.mcsv.comments.service;

import java.util.List;

import com.mx.mcsv.comments.dto.CommentDTO;
import com.mx.mcsv.comments.entity.Comment;
import com.mx.mcsv.comments.exceptions.CommentException;

public interface CommentService {

	void deleteComment(Long id, Long userId) throws CommentException;

	List<CommentDTO> findAll();

	CommentDTO findById(Long id) throws CommentException;

	List<CommentDTO> getCommentsByBlog(Long id);

	List<CommentDTO> getCommentsByUser(Long id);

	CommentDTO save(Comment comment) throws CommentException;

	CommentDTO update(Long id, Comment comment) throws CommentException;

}
