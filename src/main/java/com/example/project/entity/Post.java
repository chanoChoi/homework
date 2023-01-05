package com.example.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.example.project.dto.PostRequestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Post extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String content;

	public Post(PostRequestDto postRequestDto) {
		this.title = postRequestDto.getTitle();
		this.username = postRequestDto.getUsername();
		this.content = postRequestDto.getContent();
	}

	public void update(PostRequestDto postRequestDto){
		this.title = postRequestDto.getTitle();
		this.content = postRequestDto.getContent();
	}

}
