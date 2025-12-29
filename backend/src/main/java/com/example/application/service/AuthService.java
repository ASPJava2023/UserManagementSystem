package com.example.application.service;

import com.example.application.dto.request.LoginRequest;
import com.example.application.dto.request.ResetPasswordRequest;
import com.example.application.dto.request.SignupRequest;
import com.example.application.dto.response.LoginResponse;

public interface AuthService {
    boolean registerUser(SignupRequest request);

    LoginResponse login(LoginRequest request);

    boolean resetPassword(ResetPasswordRequest request);
}
