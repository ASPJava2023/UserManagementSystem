package com.example.application.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String message;
    private boolean isAuthenticated;
    private boolean isFirstLogin;
    private Long userId;
    private String name;
    // We can add a token here if we implement a simple token system
    // private String token;
}
