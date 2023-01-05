package com.example.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.example.project.dto.SignupRequestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity (name = "users")
@Getter
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private UserRoleEnum userRoleEnum;

	public User(SignupRequestDto requestDto){
		this.username = requestDto.getUsername();
		this.password = requestDto.getPassword();
		this.userRoleEnum = requestDto.getUserRoleEnum();
	}



}
