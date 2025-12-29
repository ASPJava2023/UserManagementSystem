package com.example.application.service;

import com.example.application.dto.response.DashboardResponse;

public interface DashboardService {
    DashboardResponse getDashboardData(String email);

    com.example.application.dto.response.QuoteApiResponse getQuote();
}
