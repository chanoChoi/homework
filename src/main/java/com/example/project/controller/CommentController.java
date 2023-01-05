package com.example.project.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

	@PostMapping("/boards/{id}/comments")
	public ResponseEntity<CommentResponseDto> createComment(@PathVariable(name = "id") Long postId,
		@RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal
	UserDetails userDetails) {
		CommentResponseDto commentResponseDto = commentService.createComment(postId, requestDto,
			userDetails.getUsername());
		return ResponseEntity.status(HttpStatus.OK).body(commentResponseDto);
	}

	@PutMapping("/comments/{commentId}")
	public ResponseEntity<CommentResponseDto> updateComment(
		@RequestBody CommentRequestDto commentRequestDto, @PathVariable Long commentId,
		@AuthenticationPrincipal
		UserDetails userDetails) {
		CommentResponseDto commentResponseDto = commentService.updateComment(commentRequestDto,
			userDetails.getUsername(), commentId);
		return ResponseEntity.status(HttpStatus.OK).body(commentResponseDto);
	}

	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<String> deleteComment(@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable Long commentId) {
		commentService.deleteComment(userDetails.getUsername(), commentId);
		return new ResponseEntity<>("삭제 성공!", HttpStatus.OK);
	}

	@GetMapping("commentlike/{commentId}")
	public ResponseEntity<String> addLike(@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable Long commentId) {
		String body = commentService.addLike(commentId, userDetails.getUsername());
		return ResponseEntity.ok(body);
	}
}