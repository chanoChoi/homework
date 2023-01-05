package com.example.project.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

	//게시물 생성
	@Transactional
	public Post createPost(PostRequestDto requestDto, String username) {
		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
		);

		Post post = requestDto.convertTo(user);
		return postRepository.save(post);
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
	public PostResponseDto updatePost(Long id, PostRequestDto requestDto, String username) {
		Post post = postRepository.findById(id).orElseThrow(
			() -> new RuntimeException("수정하려는 게시글이 없습니다.")
		);

		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

		if (post.checkAuthorization(user)) {
			post.update(requestDto);
			return new PostResponseDto(post);
		}

		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자만 삭제/수정할 수 있습니다.");
	}

	@Transactional
	public void deletePost(Long id, String username) {
		Post post = postRepository.findById(id).orElseThrow(
			() -> new RuntimeException("삭제하려는 게시글이 없습니다.")
		);
		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
		);

		if (post.checkAuthorization(user)) {
			post.getLikes().clear();
			postRepository.deleteById(id);
			return;
		}

		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자만 삭제/수정할 수 있습니다.");
	}

	@Transactional
	public String addLike(Long postId, String username) {
		Post post = postRepository.findById(postId).orElseThrow(
			() -> new RuntimeException(" 해당 게시글이 없습니다.")
		);

		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
		);

		if (!post.getLikes().contains(user)) {
			post.getLikes().add(user);
			this.postRepository.save(post);
			return "좋아요 성공!";
		} else {
			post.getLikes().remove(user);
			this.postRepository.save(post);
			return "좋아요 취소!";
		}
	}
}
