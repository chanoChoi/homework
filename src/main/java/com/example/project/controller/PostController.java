package com.example.project.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletMapping;
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

import com.example.project.dto.PostRequestDto;
import com.example.project.dto.PostResponseDto;
import com.example.project.entity.Post;
import com.example.project.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	//게시물 생성
	@PostMapping("/api/posts")
	public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto requestDto,
		@AuthenticationPrincipal
		UserDetails userDetails) {
		Post post = postService.createPost(requestDto, userDetails.getUsername());
		return ResponseEntity.ok(new PostResponseDto(post));
	}

	//게시물 전체 조회
	@GetMapping("/api/posts")
	public List<PostResponseDto> getPost() {
		List<Post> posts = postService.getPost();
		return posts.stream().map(PostResponseDto::new).collect(Collectors.toList());
	}

	//게시물 선택 조회
	@GetMapping("/api/posts/{id}")
	public PostResponseDto getPost(@PathVariable Long id) {
		return postService.getPost(id);
	}

	//게시물 수정
	@PutMapping("/api/posts/{id}")
	public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id,
		@RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal
	UserDetails userDetails) {
		PostResponseDto postResponseDto = postService.updatePost(id, postRequestDto,
			userDetails.getUsername());
		return ResponseEntity.status(HttpStatus.OK).body(postResponseDto);
	}

	//게시물 삭제
	@DeleteMapping("/api/posts/{id}")
	public ResponseEntity<String> deletePost(@PathVariable Long id, @AuthenticationPrincipal
	UserDetails userDetails) {
		postService.deletePost(id, userDetails.getUsername());
		return new ResponseEntity<>("삭제 성공!", HttpStatus.OK);
	}

	@GetMapping("/api/postlike/{postId}")
	public ResponseEntity<String> addLike(@PathVariable Long postId, @AuthenticationPrincipal
	UserDetails userDetails) {
		String body = postService.addLike(postId, userDetails.getUsername());
		return ResponseEntity.ok(body);
	}
}
