package com.example.project.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.example.project.entity.UserRoleEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequestDto {

	@Size(min = 4, max = 10)
	@Pattern(regexp = "^[a-z0-9]*$")
	@NotEmpty(message = "사용자ID는 필수항목입니다.")
	private String username;

	// @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$)")
	@Size(min= 8, max= 15)
	@Pattern(regexp = "^[a-zA-Z0-9]{8,15}$")
	@NotEmpty(message = "사용자 비밀번호는 필수항목입니다.")
	private String password;

	private UserRoleEnum userRoleEnum;

	private String adminToken = "";

	public void setUserRoleEnum(UserRoleEnum userRoleEnum) {
		this.userRoleEnum = userRoleEnum;
	}
}
