package com.example.springlogin.global.util.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public interface AuthUtil {

    Long getMemberId(HttpServletRequest request, HttpServletResponse response);
}
