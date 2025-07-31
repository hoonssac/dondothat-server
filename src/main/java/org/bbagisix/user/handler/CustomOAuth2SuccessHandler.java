package org.bbagisix.user.handler;

import lombok.RequiredArgsConstructor;
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
        String token = jwtUtil.createToken(authentication.getName(), "ROLE_USER", 60 * 60 * 1000L); // 1시간 유효

        String targetUrl = UriComponentsBuilder.fromUriString(
                "http://localhost:5173/oauth-redirect")
            .queryParam("token", token)
            .build()
            .encode(StandardCharsets.UTF_8)
            .toUriString();

        // Cookie cookie = new Cookie("jwt", token);
        // cookie.setPath("/");
        // cookie.setHttpOnly(true);
        // cookie.setSecure(request.isSecure());
        // response.addCookie(cookie);

        // getRedirectStrategy().sendRedirect(request, response, "/oauth2-success");
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
