package com.mx.mcsv.comments.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mx.mcsv.comments.dto.CommentDTO;
import com.mx.mcsv.comments.entity.Comment;
import com.mx.mcsv.comments.exceptions.CommentException;
import com.mx.mcsv.comments.repository.CommentRepository;
import com.mx.mcsv.comments.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepository commentRepository;

	@Override
	public void deleteComment(Long id) throws CommentException {
		Optional<Comment> blog = commentRepository.findById(id);

		if (blog.isPresent()) {
			commentRepository.deleteById(blog.get().getId());
			return;
		}

		throw new CommentException("Comment not found with id: " + id, HttpStatus.NOT_FOUND);

	}

	@Override
	public List<CommentDTO> findAll() {
		List<Comment> comments = (List<Comment>) commentRepository.findAll();
		return comments.stream().map(this::convertToCommentDTO).collect(Collectors.toList());
	}

	@Override
	public CommentDTO findById(Long id) throws CommentException {
		Optional<Comment> comment = commentRepository.findById(id);

		if (comment.isPresent()) {
			return convertToCommentDTO(comment.get());
		}
		throw new CommentException("Comment not found with id: " + id, HttpStatus.NOT_FOUND);
	}

	@Override
	public CommentDTO save(Comment comment) throws CommentException {

		comment.setCreatedAt(LocalDateTime.now());
		comment.setUpdatedAt(LocalDateTime.now());
		Comment commentSaved = commentRepository.save(comment);
		return convertToCommentDTO(commentSaved);
	}

	@Override
	public CommentDTO update(Long id, Comment comment) throws CommentException {
		Optional<Comment> commentOptional = commentRepository.findById(id);

		if (commentOptional.isPresent()) {
			Comment existingComment = commentOptional.get();

			existingComment.setContent(comment.getContent());
			existingComment.setUpdatedAt(LocalDateTime.now());

			commentRepository.save(existingComment);
			return convertToCommentDTO(existingComment);
		}

		throw new CommentException("Comment not found with id: " + id, HttpStatus.NOT_FOUND);
	}

	private CommentDTO convertToCommentDTO(Comment comment) {
		CommentDTO commentDTO = new CommentDTO();
		commentDTO.setId(comment.getId());
		commentDTO.setUserId(comment.getUserId());
		commentDTO.setBlogId(comment.getBlogId());
		commentDTO.setContent(comment.getContent());
		commentDTO.setCreatedAt(comment.getCreatedAt());
		return commentDTO;
	}

}
