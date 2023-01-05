package com.example.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
