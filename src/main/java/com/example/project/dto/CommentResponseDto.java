package com.example.project.dto;

import java.time.LocalDateTime;

import com.example.project.entity.Comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponseDto {

	private Long id;
	private String content;
	private int likeCount;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public CommentResponseDto(Comment comment){
		this.id = comment.getId();
		this.content = comment.getContent();
		this.createdAt =comment.getCreateAt();
		this.modifiedAt = comment.getModifiedAt();
		this.likeCount = comment.getLikes().size();
	}
}

