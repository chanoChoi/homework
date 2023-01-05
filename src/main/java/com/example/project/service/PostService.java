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
	public Post createPost(PostRequestDto requestDto, HttpServletRequest request) {
		String token = jwtUtil.resolveToken(request);
		Claims claims;

		if (token == null) {
			throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
		} else {
			if (jwtUtil.validateToken(token)) {
				claims = jwtUtil.getUserInfoFromToken(token);
			} else {
				throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
			}
		}

		User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
			() -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
		);

		Post post = requestDto.convertTo(user);
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

			if (post.checkAuthorization(user)) {
				post.update(requestDto);
			} else {
				throw new IllegalArgumentException("권한이 없음");
			}
		}
		return new PostResponseDto(post);
	}

	@Transactional
	public void deletePost(Long id, HttpServletRequest request){
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

			if (post.checkAuthorization(user)) {
				postRepository.deleteById(id);
			} else {
				throw new IllegalArgumentException("권한이 없음");
			}
		}
	}

	@Transactional
	public ResponseEntity addLike(Long postId, HttpServletRequest request) {
		User user = new User();
		Post post = postRepository.findById(postId).orElseThrow(
			() -> new RuntimeException(" 해당 게시글이 없습니다.")
		);

		String token = jwtUtil.resolveToken(request);
		Claims claims;

		if (token != null) {
			if (jwtUtil.validateToken(token)) {
				claims = jwtUtil.getUserInfoFromToken(token);
			} else {
				throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
			}
			user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
				() -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
			);
		}
		if (!post.getLikes().contains(user)) {
			post.getLikes().add(user);
			post.getLikes().size();
			this.postRepository.save(post);
			return new ResponseEntity<>("좋아요 성공!", HttpStatus.OK);
		} else {
			post.getLikes().remove(user);
			post.getLikes().size();
			this.postRepository.save(post);
			return new ResponseEntity<>("좋아요 취소!", HttpStatus.OK);
		}
	}
}
