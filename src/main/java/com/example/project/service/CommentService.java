package com.example.project.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.project.dto.CommentRequestDto;
import com.example.project.dto.CommentResponseDto;
import com.example.project.entity.Comment;
import com.example.project.entity.Post;
import com.example.project.entity.User;
import com.example.project.entity.UserRoleEnum;
import com.example.project.jwt.JwtUtil;
import com.example.project.repository.CommentRepository;
import com.example.project.repository.PostRepository;
import com.example.project.repository.UserRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;

	@Transactional
	public CommentResponseDto createComment(Long PostId, CommentRequestDto requestDto,
		String username) {
		Post post = postRepository.findById(PostId).orElseThrow(
			() -> new IllegalArgumentException("게시글을 찾을 수 없습니다.")
		); //해당 게시글 찾는 과정

		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

		Comment comment = new Comment(requestDto, user, post);
		commentRepository.save(comment);
		return new CommentResponseDto(comment);
	}

	@Transactional
	public CommentResponseDto updateComment(CommentRequestDto requestDto,
		String username, Long commentId) {
		Comment comment = commentRepository.findById(commentId).orElseThrow(
			() -> new IllegalArgumentException("수정할 댓글이 없습니다.")
		);

		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new IllegalArgumentException("사용자가 존재하지 않습니다.")

		);

		if (comment.validateAuth(user) || user.getUserRoleEnum().equals(UserRoleEnum.ADMIN)) {
			comment.update(requestDto);
			return new CommentResponseDto(comment);
		}

		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자만 삭제/수정할 수 있습니다.");

	}

	@Transactional
	public void deleteComment(String username, Long commentId) {
		Comment comment = commentRepository.findById(commentId).orElseThrow(
			() -> new IllegalArgumentException("삭제할 댓글이 없습니다.")
		);

		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new IllegalArgumentException("삭제할 댓글이 존재하지 않습니다."));

		if (user.getUserRoleEnum().equals(UserRoleEnum.ADMIN) || comment.validateAuth(user)) {
			commentRepository.deleteById(commentId);
			return;
		}

		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자만 삭제/수정할 수 있습니다.");
	}

	@Transactional
	public String addLike(Long commentId, String username) {
		Comment comment = commentRepository.findById(commentId).orElseThrow(
			() -> new RuntimeException("해당 게시글이 없습니다.")
		);

		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
		);

		if (!comment.getLikes().contains(user)) {
			comment.getLikes().add(user);
			this.commentRepository.save(comment);
			return "좋아요 성공!";
		} else {
			comment.getLikes().remove(user);
			this.commentRepository.save(comment);
			return "좋아요 취소!";
		}
	}
}