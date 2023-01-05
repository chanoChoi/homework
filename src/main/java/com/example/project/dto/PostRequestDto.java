package com.example.project.dto;

import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestDto {

	private String title;
	private String username;
	private String content;

}
