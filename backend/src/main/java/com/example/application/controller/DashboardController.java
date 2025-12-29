package com.example.application.controller;

import com.example.application.dto.response.ApiResponse;
import com.example.application.dto.response.DashboardResponse;
import com.example.application.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard(java.security.Principal principal) {
        String email = principal.getName();
        DashboardResponse response = dashboardService.getDashboardData(email);
        return ResponseEntity.ok(new ApiResponse<>(true, "Dashboard data fetched", response));
    }

    @GetMapping("/dashboard/quote")
    public ResponseEntity<ApiResponse<com.example.application.dto.response.QuoteApiResponse>> getQuote() {
        com.example.application.dto.response.QuoteApiResponse quote = dashboardService.getQuote();
        return ResponseEntity.ok(new ApiResponse<>(true, "Quote fetched", quote));
    }
}
