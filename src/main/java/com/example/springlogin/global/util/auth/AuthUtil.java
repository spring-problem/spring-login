package com.example.springlogin.global.util.auth;

import jakarta.servlet.http.HttpServletRequest;


public interface AuthUtil {

    Long getMemberId(HttpServletRequest request);
}
