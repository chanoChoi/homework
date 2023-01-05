package com.example.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post>findAllByOrderByModifiedAtDesc();
}
