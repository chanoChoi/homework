package com.example.project.security;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import com.example.project.jwt.JwtUtil;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String token = JwtUtil.resolveToken(request);

		if (Objects.nonNull(token)) {
			if (!JwtUtil.validateToken(token)) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST ,"토큰이 유효하지 않습니다.");
			}
			Claims claims = JwtUtil.getUserInfoFromToken(token);
			String username = claims.getSubject();
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			Authentication authenticated = UsernamePasswordAuthenticationToken.authenticated(
				userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authenticated);
		}

		filterChain.doFilter(request, response);
	}
}
