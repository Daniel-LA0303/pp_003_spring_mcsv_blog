package com.mx.mcsv.blog.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mx.mcsv.blog.dto.BlogDTO;
import com.mx.mcsv.blog.entity.Blog;
import com.mx.mcsv.blog.exception.BlogException;
import com.mx.mcsv.blog.repository.BlogRepository;
import com.mx.mcsv.blog.service.BlogService;

@Service
public class BlogServiceImpl implements BlogService {

	@Autowired
	private BlogRepository blogRepository;

	@Override
	public void deleteBlog(Long id) throws BlogException {
		Optional<Blog> blog = blogRepository.findById(id);

		if (blog.isPresent()) {
			blogRepository.deleteById(blog.get().getId());
			return;
		}

		throw new BlogException("Blog not found with id: " + id, HttpStatus.NOT_FOUND);

	}

	@Override
	public List<BlogDTO> findAll() {
		List<Blog> blogs = (List<Blog>) blogRepository.findAll();
		return blogs.stream().map(this::convertToBlogDTO).collect(Collectors.toList());
	}

	@Override
	public BlogDTO findById(Long id) throws BlogException {
		Optional<Blog> blog = blogRepository.findById(id);

		if (blog.isPresent()) {
			return convertToBlogDTO(blog.get());
		}
		throw new BlogException("Blog not found with id: " + id, HttpStatus.NOT_FOUND);
	}

	@Override
	public BlogDTO save(Blog blog) throws BlogException {
		Optional<Blog> existingBlog = blogRepository.findByTitle(blog.getTitle());
		if (existingBlog.isPresent()) {
			throw new BlogException("A blog with this title already exists", HttpStatus.BAD_REQUEST);
		}

		blog.setCreatedAt(LocalDateTime.now());
		blog.setUpdatedAt(LocalDateTime.now());
		Blog savedBlog = blogRepository.save(blog);
		return convertToBlogDTO(savedBlog);
	}

	@Override
	public BlogDTO update(Long id, Blog blog) throws BlogException {
		Optional<Blog> blogOptional = blogRepository.findById(id);

		if (blogOptional.isPresent()) {
			Blog existingBlog = blogOptional.get();

			if (!existingBlog.getTitle().equals(blog.getTitle())) {
				Optional<Blog> existingTitle = blogRepository.findByTitle(blog.getTitle());
				if (existingTitle.isPresent()) {
					throw new BlogException("A blog with this title already exists", HttpStatus.BAD_REQUEST);
				}
			}

			existingBlog.setTitle(blog.getTitle());
			existingBlog.setDescription(blog.getDescription());
			existingBlog.setContent(blog.getContent());
			existingBlog.setStatus(blog.getStatus());
			existingBlog.setUpdatedAt(LocalDateTime.now());

			blogRepository.save(existingBlog);
			return convertToBlogDTO(existingBlog);
		}

		throw new BlogException("Blog not found with id: " + id, HttpStatus.NOT_FOUND);
	}

	private BlogDTO convertToBlogDTO(Blog blog) {
		BlogDTO blogDTO = new BlogDTO();
		blogDTO.setId(blog.getId());
		blogDTO.setTitle(blog.getTitle());
		blogDTO.setDescription(blog.getDescription());
		blogDTO.setContent(blog.getContent());
		blogDTO.setUserId(blog.getUserId());
		blogDTO.setStatus(blog.getStatus());
		blogDTO.setCreatedAt(blog.getCreatedAt());
		return blogDTO;
	}

}
