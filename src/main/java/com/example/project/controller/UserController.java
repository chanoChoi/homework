package com.example.project.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.project.dto.LoginRequestDto;
import com.example.project.dto.SignupRequestDto;
import com.example.project.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@Valid SignupRequestDto signupRequestDto) {
		userService.signup(signupRequestDto);
		return new ResponseEntity<>("회원가입이 완료되었습니다.", HttpStatus.CREATED);
	}

	@ResponseBody
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
		userService.login(loginRequestDto, response);
		return new ResponseEntity<>("로그인이 성공하였습니다.", HttpStatus.OK);
	}

}
