package com.example.project.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.example.project.dto.CommentRequestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String content;

	@ManyToOne
	@JoinColumn(name = "USER_ID", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "Post_ID",nullable = false)
	private Post post;

	@ManyToMany
	private List<User> likes = new ArrayList<>();



	public Comment (CommentRequestDto requestDto, User user, Post post){
		this.content = requestDto.getContent();
		this.post = post;
		this.user = user;
	}

	public void update(CommentRequestDto requestDto){
		this.content = requestDto.getContent();
	}

	public boolean validateAuth(User user) {
		return Objects.equals(this.user.getId(), user.getId());
	}
}