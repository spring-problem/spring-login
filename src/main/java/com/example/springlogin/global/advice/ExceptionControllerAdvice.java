package com.example.springlogin.global.advice;

import com.example.springlogin.global.exception.JwtParsingFailException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(JwtParsingFailException.class)
    public String handleJwtParsingFailException() {
        return "index";
    }
}
