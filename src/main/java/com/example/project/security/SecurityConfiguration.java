package com.example.project.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {
	private final UserDetailsService userDetailsService;
	@Bean JwtAuthFilter jwtAuthFilter() {
		return new JwtAuthFilter(userDetailsService);
	}

	@Bean JwtEntryPoint jwtEntryPoint() {
		return new JwtEntryPoint();
	}

	@Bean
	public WebSecurityCustomizer ignoringCustomizer() {
		return web -> web.ignoring().antMatchers("/h2-console/**");
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling().authenticationEntryPoint(jwtEntryPoint());

		http.authorizeHttpRequests(auth -> auth
			.antMatchers(HttpMethod.GET, "/api/posts/**")
			.permitAll()
			.antMatchers(HttpMethod.POST, "/api/user/**")
			.permitAll()
			.anyRequest().authenticated()
		);

		return http.build();
	}
}
