package com.example.springlogin.member.controller;

import com.example.springlogin.member.controller.request.JoinRequest;
import com.example.springlogin.member.controller.request.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public interface MemberController {

    @GetMapping("/")
    String getHomepage(HttpServletRequest request, HttpServletResponse response, Model model);

    @GetMapping("/login")
    String getLoginPage(HttpServletRequest request, HttpServletResponse response, Model model);

    @PostMapping("/login")
    String login(@ModelAttribute LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response, Model model);

    @PostMapping("/logout")
    String logout(HttpServletRequest request, HttpServletResponse response);

    @GetMapping("/join")
    String getJoinPage(HttpServletRequest request, HttpServletResponse response, Model model);

    @PostMapping("/join")
    String join(@ModelAttribute JoinRequest joinRequest, HttpServletRequest request, HttpServletResponse response);

}
