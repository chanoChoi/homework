package com.example.project.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	private final JwtUtil jwtUtil;

	@Transactional
	public CommentResponseDto createComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {

		String token = jwtUtil.resolveToken(request);
		Claims claims;

		Post post = postRepository.findById(id).orElseThrow(
			()-> new IllegalArgumentException("게시글을 찾을 수 없습니다.")
		); //해당 게시글 찾는 과정

		if (token == null) {
			throw new IllegalArgumentException("권한 없음");
		}

		if (jwtUtil.validateToken(token)) {
			claims = jwtUtil.getUserInfoFromToken(token);
		} else {
			throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
		}
		User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
			() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
		Comment comment = new Comment(requestDto, user, post);
		commentRepository.save(comment);
		return new CommentResponseDto(comment);
	}

	@Transactional
	public CommentResponseDto updateComment( CommentRequestDto requestDto, HttpServletRequest request, Long commentId) {
		//        Board board = boardRepository.findById(id).orElseThrow(
		//                ()-> new IllegalArgumentException("게시글을 찾을 수 없습니다.")
		//        );
		Comment comment = commentRepository.findById(commentId).orElseThrow(
			() -> new IllegalArgumentException("수정할 댓글이 없습니다.")
		);

		String token = jwtUtil.resolveToken(request);
		Claims claims;


		if (token != null) {
			if (jwtUtil.validateToken(token)) {
				claims = jwtUtil.getUserInfoFromToken(token);
			} else {
				throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
			}
			User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
				() -> new IllegalArgumentException("사용자가 존재하지 않습니다.")

			);
			if (comment.getUser().getId().equals(user.getId())|| user.getUserRoleEnum().equals(UserRoleEnum.ADMIN)) {
				comment.update(requestDto);
			}else{
				throw new IllegalArgumentException("댓글을 수정할 권한이 없습니다.");
			}
		}
		return new CommentResponseDto(comment);
	}

	@Transactional
	public void deleteComment( HttpServletRequest request, Long commentId){
		//        Board board = boardRepository.findById(id).orElseThrow(
		//                ()-> new IllegalArgumentException("게시글을 찾을 수 없습니다.")
		//        );

		Comment comment = commentRepository.findById(commentId).orElseThrow(
			() -> new IllegalArgumentException("삭제할 댓글이 없습니다.")
		);

		String token = jwtUtil.resolveToken(request);
		Claims claims;

		if (token != null){
			if(jwtUtil.validateToken(token)){
				claims = jwtUtil.getUserInfoFromToken(token);
			}else{
				throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
			}
			User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
				()-> new IllegalArgumentException("삭제할 댓글이 존재하지 않습니다.")
			);
			if (comment.getUser().getId().equals(user.getId())|| user.getUserRoleEnum().equals(UserRoleEnum.ADMIN)) {
				commentRepository.deleteById(commentId);
			}else{
				throw new IllegalArgumentException("댓글을 삭제할 권한이 없습니다.");
			}
		}
	}

	@Transactional
	public ResponseEntity addLike(Long commentId, HttpServletRequest request) {
		User user = new User();
		Comment comment = commentRepository.findById(commentId).orElseThrow(
			() -> new RuntimeException("해당 게시글이 없습니다.")
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
		if (!comment.getLikes().contains(user)) {
			comment.getLikes().add(user);
			comment.getLikes().size();
			this.commentRepository.save(comment);
			return new ResponseEntity<>("좋아요 성공!", HttpStatus.OK);
		} else {
			comment.getLikes().remove(user);
			comment.getLikes().size();
			this.commentRepository.save(comment);
			return new ResponseEntity<>("좋아요 취소!", HttpStatus.OK);
		}
	}
}