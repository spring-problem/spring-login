package com.example.springlogin.global.advice;

import com.example.springlogin.global.exception.AuthException;
import com.example.springlogin.global.exception.handler.AuthExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionControllerAdvice {

    final AuthExceptionHandler authExceptionHandler;

    @ExceptionHandler(AuthException.class)
    public String handleAuthException(HttpServletRequest request, HttpServletResponse response) {
        authExceptionHandler.handleAuthException(request, response);
        return "index";
    }
}
