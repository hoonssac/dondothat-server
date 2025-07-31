package org.bbagisix.user.handler;

import lombok.RequiredArgsConstructor;

import org.bbagisix.user.dto.CustomOAuth2User;
import org.bbagisix.user.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        String role = oAuth2User.getRole();
        String name = oAuth2User.getName();
        String nickname = oAuth2User.getNickname();
        String token = jwtUtil.createToken(email, role, name, nickname, 60 * 60 * 1000L); // 1시간 유효

        String targetUrl = UriComponentsBuilder.fromUriString(
                "http://localhost:5173/oauth-redirect")
            .queryParam("token", token)
            .build()
            .encode(StandardCharsets.UTF_8)
            .toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
