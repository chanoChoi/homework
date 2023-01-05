package com.example.project.controller;

import java.util.List;

import javax.servlet.http.HttpServletMapping;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	@PostMapping("/api/post")
	public Post createPost(@RequestBody PostRequestDto requestDto){
		return postService.createPost(requestDto);
	}

	//게시물 전체 조회
	@GetMapping("/api/posts")
	public List<Post> getPost(){
		return postService.getPost();
	}

	//게시물 선택 조회
	@GetMapping("/api/post/{id}")
	public PostResponseDto getPost(@PathVariable Long id){
		return postService.getPost(id);
	}

	//게시물 수정
	@PutMapping("/api/post/{id}")
	public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto, HttpServletRequest request){
		PostResponseDto postResponseDto = postService.updatePost(id,postRequestDto,request);
		return ResponseEntity.status(HttpStatus.OK).body(postResponseDto);
	}

	//게시물 삭제
	@DeleteMapping("/api/post/{id}")
	public ResponseEntity deletePost(@PathVariable Long id, HttpServletRequest request){
		return postService.deletePost(id,request);
	}
}
