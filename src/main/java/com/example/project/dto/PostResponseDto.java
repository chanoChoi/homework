package com.example.project.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.project.entity.Post;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostResponseDto {

	private String title;
	private String username;
	private String content;
	private LocalDateTime createAt;
	private LocalDateTime modifiedAt;
	private List<CommentResponseDto> comments;

	public PostResponseDto (Post post){
		this.title = post.getTitle();
		this.username = post.getUser().getUsername();
		this.content = post.getContent();
		this.createAt = post.getCreateAt();
		this.modifiedAt = post.getModifiedAt();
		this.comments = post.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
	}
}