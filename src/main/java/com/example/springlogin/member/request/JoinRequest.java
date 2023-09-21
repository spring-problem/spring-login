package com.example.springlogin.member.request;


import lombok.Builder;
import lombok.Data;

@Data
public class JoinRequest {
    private String email;
    private String password;

}
