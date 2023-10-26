package com.example.springlogin.config;

import com.example.springlogin.config.auth.JwtAuthConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(JwtAuthConfig.class)
public class ApplicationConfig {
}