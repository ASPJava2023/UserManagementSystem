package com.example.application.controller;

import com.example.application.dto.request.LoginRequest;
import com.example.application.dto.request.ResetPasswordRequest;
import com.example.application.dto.request.SignupRequest;
import com.example.application.dto.response.ApiResponse;
import com.example.application.dto.response.LoginResponse;
import com.example.application.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Boolean>> registerUser(@Valid @RequestBody SignupRequest request) {
        boolean success = authService.registerUser(request);
        return new ResponseEntity<>(
                new ApiResponse<>(success, "User registered successfully, check email for password.", success),
                HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return new ResponseEntity<>(new ApiResponse<>(true, response.getMessage(), response), HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Boolean>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        boolean success = authService.resetPassword(request);
        return new ResponseEntity<>(new ApiResponse<>(success, "Password reset successfully. Please login.", success),
                HttpStatus.OK);
    }
}
