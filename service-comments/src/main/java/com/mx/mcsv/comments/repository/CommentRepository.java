package com.mx.mcsv.comments.repository;

import org.springframework.data.repository.CrudRepository;

import com.mx.mcsv.comments.entity.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {

}
