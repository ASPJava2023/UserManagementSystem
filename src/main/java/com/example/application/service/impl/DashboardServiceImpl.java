package com.example.application.service.impl;

import com.example.application.dto.response.DashboardResponse;
import com.example.application.dto.response.QuoteApiResponse;
import com.example.application.entity.User;
import com.example.application.exception.ResourceNotFoundException;
import com.example.application.feign.QuoteClient;
import com.example.application.repository.UserRepository;
import com.example.application.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final QuoteClient quoteClient;

    @Override
    public DashboardResponse getDashboardData(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String quote = "Stay positive!";
        try {
            QuoteApiResponse apiResponse = quoteClient.getRandomQuote();
            if (apiResponse != null && apiResponse.getContent() != null) {
                quote = apiResponse.getContent() + " - " + apiResponse.getAuthor();
            }
        } catch (Exception e) {
            log.error("Quote Service Unavailable: {}", e.getMessage());
            quote = "Quote service unavailable. Stay positive!";
        }

        return DashboardResponse.builder()
                .userName(user.getName())
                .quote(quote)
                .logoutFlag(false) // Default logic, maybe true if some condition?
                .build();
    }
}
