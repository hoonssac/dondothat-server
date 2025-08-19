package org.bbagisix.user.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    private static final String JWT_COOKIE_NAME = "accessToken";
    private static final int COOKIE_MAX_AGE = 24 * 60 * 60; // 24시간 (초 단위)

    public static void addJwtCookie(HttpServletResponse response, String token, HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String sameSitePolicy = getSameSitePolicy(userAgent);
        
        String cookieHeader = String.format(
            "%s=%s; Path=/; Max-Age=%d; HttpOnly; Secure%s",
            JWT_COOKIE_NAME,
            token,
            COOKIE_MAX_AGE,
            sameSitePolicy
        );
        response.addHeader("Set-Cookie", cookieHeader);
        
        // 삼성 브라우저/Safari용 추가 쿠키 (SameSite 없음)
        if (isSamsungBrowser(userAgent) || isSafari(userAgent)) {
            String fallbackCookieHeader = String.format(
                "%s=%s; Path=/; Max-Age=%d; HttpOnly; Secure",
                JWT_COOKIE_NAME + "_fallback",
                token,
                COOKIE_MAX_AGE
            );
            response.addHeader("Set-Cookie", fallbackCookieHeader);
        }
    }
    
    private static String getSameSitePolicy(String userAgent) {
        if (userAgent == null) {
            return "; SameSite=Lax";
        }
        
        // 삼성 브라우저나 Safari는 SameSite=Lax 사용
        if (isSamsungBrowser(userAgent) || isSafari(userAgent)) {
            return "; SameSite=Lax";
        }
        
        // 크롬은 SameSite=None 사용
        return "; SameSite=None";
    }
    
    private static boolean isSamsungBrowser(String userAgent) {
        return userAgent != null && userAgent.contains("SamsungBrowser");
    }
    
    private static boolean isSafari(String userAgent) {
        return userAgent != null && 
               userAgent.contains("Safari") && 
               !userAgent.contains("Chrome") && 
               !userAgent.contains("Chromium");
    }

    public static void deleteJwtCookie(HttpServletResponse response) {
        // 기본 쿠키 삭제
        String cookieHeader = String.format(
            "%s=; Path=/; Max-Age=0; HttpOnly; Secure; SameSite=None; Expires=Thu, 01 Jan 1970 00:00:00 GMT",
            JWT_COOKIE_NAME
        );
        response.addHeader("Set-Cookie", cookieHeader);
        
        // fallback 쿠키도 삭제
        String fallbackCookieHeader = String.format(
            "%s=; Path=/; Max-Age=0; HttpOnly; Secure; Expires=Thu, 01 Jan 1970 00:00:00 GMT",
            JWT_COOKIE_NAME + "_fallback"
        );
        response.addHeader("Set-Cookie", fallbackCookieHeader);
        
        System.out.println("쿠키 삭제 요청 전송됨: " + JWT_COOKIE_NAME);
    }
}
