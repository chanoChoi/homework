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
	private Long id;
	private String title;
	private String username;
	private String content;
	private LocalDateTime createAt;
	private LocalDateTime modifiedAt;
	private int likeCount;
	private List<CommentResponseDto> comments;

	public PostResponseDto (Post post){
		this.id = post.getId();
		this.title = post.getTitle();
		this.username = post.getUser().getUsername();
		this.content = post.getContent();
		this.createAt = post.getCreateAt();
		this.modifiedAt = post.getModifiedAt();
		this.comments = post.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
		this.likeCount = post.getLikes().size();
	}
}