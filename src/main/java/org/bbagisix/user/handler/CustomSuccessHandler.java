package org.bbagisix.user.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bbagisix.user.dto.CustomOAuth2User;
import org.bbagisix.user.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtUtil jwtUtil;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();

		String name = customOAuth2User.getName();
		String role = customOAuth2User.getRole();
		String token = jwtUtil.createToken(name, role, 60 * 60 * 1000L);

		String redirectUrl = "https://dondothat.netlify.app/oauth-redirect?token=" + token;
		response.sendRedirect(redirectUrl);
	}
}
