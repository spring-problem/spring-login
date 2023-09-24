package com.example.springlogin.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;

public class MemberCookieController implements MemberController {

    @Override
    public String getLoginPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        return null;
    }

    @Override
    public String login(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
