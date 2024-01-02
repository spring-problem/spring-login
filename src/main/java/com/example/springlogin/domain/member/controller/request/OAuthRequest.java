package com.example.springlogin.domain.member.controller.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OAuthRequest {
    private String code;
    private String state;
    private String error;
    private String error_descript;
}
