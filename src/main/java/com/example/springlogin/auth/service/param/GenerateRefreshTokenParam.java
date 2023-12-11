package com.example.springlogin.auth.service.param;

import lombok.Data;

@Data
public class GenerateRefreshTokenParam {
    String token;
    Long memberId;
}
