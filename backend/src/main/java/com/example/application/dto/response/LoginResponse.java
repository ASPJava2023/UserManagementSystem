package com.example.application.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String message;
    private boolean isAuthenticated;
    private boolean passwordResetRequired;
    private Long userId;
    private String name;
    private String token;
    private String role;
}
