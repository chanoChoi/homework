package com.example.project.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Builder
public class LoginRequestDto {
	@Size(min = 4, max = 10)
	@Pattern(regexp = "^[a-z0-9]*$")
	@NotEmpty(message = "사용자ID는 필수항목입니다.")
	private String username;

	@Size(min= 8, max= 15)
	// @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$)")
	@NotEmpty(message = "사용자 비밀번호는 필수항목입니다.")
	private String password;
}
