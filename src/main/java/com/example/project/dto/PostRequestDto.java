package com.example.project.dto;

import org.springframework.stereotype.Service;

import com.example.project.entity.Post;
import com.example.project.entity.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestDto {

	private String title;
	private String content;

	public Post convertTo(User user) {
		return new Post(user, this.title, this.content);
	}
}