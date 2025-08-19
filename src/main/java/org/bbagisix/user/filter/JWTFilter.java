package org.bbagisix.user.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bbagisix.user.dto.CustomOAuth2User;
import org.bbagisix.user.dto.UserResponse;
import org.bbagisix.user.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws
		ServletException, IOException {

		String requestURI = request.getRequestURI();

		String token = null;
		
		// 1. Authorization 헤더에서 토큰 확인
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			token = authorizationHeader.split(" ")[1];
		}
		
		// 2. Authorization 헤더에 토큰이 없다면 쿠키에서 확인
		if (token == null) {
			token = getTokenFromCookie(request);
		}

		// 토큰이 없으면 다음 필터로 넘어감
		if (token == null) {
			filterChain.doFilter(request, response);
			return;
		}

		// 토큰 만료 확인
		if (jwtUtil.isExpired(token)) {
			filterChain.doFilter(request, response);
			return;
		}

		// JWT에서 사용자 정보 추출
		String name = jwtUtil.getName(token);
		String role = jwtUtil.getRole(token);
		String email = jwtUtil.getEmail(token);
		String nickname = jwtUtil.getNickname(token);
		Long userId = jwtUtil.getUserId(token);

		UserResponse userResponse = UserResponse.builder()
			.userId(userId)
			.name(name)
			.nickname(nickname)
			.email(email)
			.role(role)
			.assetConnected(false)
			.build();

		CustomOAuth2User customOAuth2User = new CustomOAuth2User(userResponse);

		Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null,
			customOAuth2User.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authToken);
		filterChain.doFilter(request, response);
	}
	
	/**
	 * 쿠키에서 JWT 토큰 추출 (fallback 쿠키 포함)
	 */
	private String getTokenFromCookie(HttpServletRequest request) {
		if (request.getCookies() != null) {
			String token = null;
			String fallbackToken = null;
			
			for (Cookie cookie : request.getCookies()) {
				if ("accessToken".equals(cookie.getName())) {
					token = cookie.getValue();
				} else if ("accessToken_fallback".equals(cookie.getName())) {
					fallbackToken = cookie.getValue();
				}
			}
			
			// 기본 토큰이 있으면 사용, 없으면 fallback 토큰 사용
			return token != null ? token : fallbackToken;
		}
		return null;
	}
}
