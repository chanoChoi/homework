package com.example.project.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.CommentRequestDto;
import com.example.project.dto.CommentResponseDto;
import com.example.project.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/post/comments/{id}")
	public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request){
		CommentResponseDto commentResponseDto = commentService.createComment(id, requestDto, request);
		return ResponseEntity.status(HttpStatus.OK).body(commentResponseDto);
	}
	@PutMapping("/comments/{commentId}")
	public ResponseEntity<CommentResponseDto> updateComment( @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request, @PathVariable Long commentId ){
		CommentResponseDto commentResponseDto = commentService.updateComment(commentRequestDto,request,commentId);
		return ResponseEntity.status(HttpStatus.OK).body(commentResponseDto);
	}
	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity deleteComment(HttpServletRequest request, @PathVariable Long commentId){
		return commentService.deleteComment(request,commentId);
	}
}
