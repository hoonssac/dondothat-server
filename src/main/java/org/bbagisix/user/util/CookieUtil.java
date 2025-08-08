package org.bbagisix.user.util;

import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    private static final String JWT_COOKIE_NAME = "accessToken";
    private static final int COOKIE_MAX_AGE = 24 * 60 * 60; // 24시간 (초 단위)

    public static void addJwtCookie(HttpServletResponse response, String token) {
        String cookieHeader = String.format(
            "%s=%s; Path=/; Max-Age=%d; HttpOnly; Secure; SameSite=None",
            JWT_COOKIE_NAME,
            token,
            COOKIE_MAX_AGE
        );
        response.addHeader("Set-Cookie", cookieHeader);
    }

    public static void deleteJwtCookie(HttpServletResponse response) {
        String cookieHeader = String.format(
            "%s=; Path=/; Max-Age=0; HttpOnly; Secure; SameSite=None; Expires=Thu, 01 Jan 1970 00:00:00 GMT",
            JWT_COOKIE_NAME
        );
        response.addHeader("Set-Cookie", cookieHeader);
        System.out.println("쿠키 삭제 요청 전송됨: " + JWT_COOKIE_NAME);
    }
}
