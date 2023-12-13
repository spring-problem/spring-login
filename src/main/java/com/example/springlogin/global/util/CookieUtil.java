package com.example.springlogin.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {


    /**
     * cookie 의 key 를 가지고 value 를 갖고 온다.
     *
     * @param request
     * @param cookieKey
     * @return 값 존재 시 value 를 return, 없다면 null 을 return 함
     */
    public static String getValue(HttpServletRequest request, String cookieKey) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (!cookieKey.equals(cookie.getName())) {
                continue;
            }
            return cookie.getValue();
        }
        return null;
    }

    /**
     * 쿠키를 삭제한다
     *
     * @param response
     * @param cookieKey
     */
    public static void expireCookie(HttpServletResponse response, String cookieKey) {
        Cookie cookie = new Cookie(cookieKey, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
