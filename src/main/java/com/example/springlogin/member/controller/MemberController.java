package com.example.springlogin.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public interface MemberController {

    @GetMapping("/login")
    String getLoginPage(HttpServletRequest request, HttpServletResponse response, Model model);

    @PostMapping("/login")
    String login(HttpServletRequest request, HttpServletResponse response);

    @PostMapping("/logout")
    String logout(HttpServletRequest request, HttpServletResponse response);
}
