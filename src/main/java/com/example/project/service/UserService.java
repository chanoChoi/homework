package com.example.project.service;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.dto.LoginRequestDto;
import com.example.project.dto.SignupRequestDto;
import com.example.project.entity.User;
import com.example.project.entity.UserRoleEnum;
import com.example.project.jwt.JwtUtil;
import com.example.project.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

	@Transactional
	public void signup(SignupRequestDto signupRequestDto) {
		String username = signupRequestDto.getUsername();
		/*	String password = signupRequestDto.getPassword();*/

		// 회원 중복 확인
		Optional<User> found = userRepository.findByUsername(username);
		if (found.isPresent()) {
			throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
		}

		if(signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)){ //회원가입 dto에 저장된 토큰이랑
			signupRequestDto.setUserRoleEnum(UserRoleEnum.ADMIN);//admin 권한 넣어줘 디티오에

		}else if(signupRequestDto.getAdminToken().equals("")){
			signupRequestDto.setUserRoleEnum(UserRoleEnum.USER);

		}else{
			throw new IllegalArgumentException("토큰이 일치하지 않습니다.");
		}

		User user = new User(signupRequestDto);
		userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
		String username = loginRequestDto.getUsername();
		String password = loginRequestDto.getPassword();

		// 사용자 확인
		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new IllegalArgumentException("등록된 사용자가 없습니다.")
		);
		// 비밀번호 확인
		if (user.validatePassword(password)) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getUserRoleEnum()));
	}
}