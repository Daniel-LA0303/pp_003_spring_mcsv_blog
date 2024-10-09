package com.mx.mcsv.blog.service;

import java.util.List;

import com.mx.mcsv.blog.dto.BlogDTO;
import com.mx.mcsv.blog.entity.Blog;
import com.mx.mcsv.blog.exception.BlogException;

public interface BlogService {

	void deleteBlog(Long id) throws BlogException;

	List<BlogDTO> findAll();

	BlogDTO findById(Long id) throws BlogException;

	BlogDTO save(Blog blog) throws BlogException;

	BlogDTO update(Long id, Blog blog) throws BlogException;

}
