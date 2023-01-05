package com.example.project.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.dto.PostRequestDto;
import com.example.project.dto.PostResponseDto;
import com.example.project.entity.Post;
import com.example.project.entity.User;
import com.example.project.entity.UserRoleEnum;
import com.example.project.jwt.JwtUtil;
import com.example.project.repository.PostRepository;
import com.example.project.repository.UserRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	//게시물 생성
	@Transactional
	public Post createPost(PostRequestDto requestDto) {
		Post post = new Post(requestDto);
		postRepository.save(post);
		return post;
	}

	//게시물 전체 조회
	@Transactional(readOnly = true)
	public List<Post> getPost() {
		return postRepository.findAllByOrderByModifiedAtDesc();
	}

	@Transactional
	public PostResponseDto getPost(Long id) {
		Post post = postRepository.findById(id).orElseThrow(
			() -> new RuntimeException("찾으시는 게시글이 없습니다.")
		);
		return new PostResponseDto(post);
	}

	@Transactional
	public PostResponseDto updatePost(Long id, PostRequestDto requestDto, HttpServletRequest request) {
		Post post = postRepository.findById(id).orElseThrow(
			() -> new RuntimeException("수정하려는 게시글이 없습니다.")
		);

		String token = jwtUtil.resolveToken(request);
		Claims claims;

		if (token != null){
			if (jwtUtil.validateToken(token)){
				claims = jwtUtil.getUserInfoFromToken(token);
			} else {
				throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
			}
			User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
				() -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
			);
			if (user.getUserRoleEnum().equals(UserRoleEnum.ADMIN)) {
				userRepository.deleteById(id);
			} else {
				throw new IllegalArgumentException("게시글을 수정할 권한이 없습니다.");
			}
		}
		return new PostResponseDto(post);
	}

	@Transactional
	public ResponseEntity deletePost(Long id, HttpServletRequest request){
		 Post post = postRepository.findById(id).orElseThrow(
			() -> new RuntimeException("삭제하려는 게시글이 없습니다.")
		);

		String token = jwtUtil.resolveToken(request);
		Claims claims;

		if (token != null){
			if (jwtUtil.validateToken(token)){
				claims = jwtUtil.getUserInfoFromToken(token);
			} else {
				throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
			}
			User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
				() -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
			);
			if (user.getUserRoleEnum().equals(UserRoleEnum.ADMIN)) {
				userRepository.deleteById(id);
			} else {
				throw new IllegalArgumentException("게시글을 삭제할 권한이 없습니다.");
			}
		}
		return new ResponseEntity<>("삭제 성공!", HttpStatus.OK);
	}
}
