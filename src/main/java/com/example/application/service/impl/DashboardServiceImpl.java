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

        QuoteApiResponse apiResponse = getQuote();
        String quoteStr = (apiResponse != null && apiResponse.getContent() != null)
                ? apiResponse.getContent() + " - " + apiResponse.getAuthor()
                : "Stay positive!";

        return DashboardResponse.builder()
                .userName(user.getName())
                .quote(quoteStr)
                .logoutFlag(false)
                .build();
    }

    @Override
    public QuoteApiResponse getQuote() {
        try {
            return quoteClient.getRandomQuote();
        } catch (Exception e) {
            log.error("Quote Service Unavailable: {}", e.getMessage());
            QuoteApiResponse fallback = new QuoteApiResponse();
            fallback.setContent(
                    "देह शिवा बरु मोहि इहै सुभ करमन ते कबहूँ न टरों। न डरों अरि सो जब जाय लरों, निश्चय करि अपुनि जीत करों। अरु सिख हों आपने ही मन कौ, इह लालच हउ गुन तउ उचरों। जब आव की अवध निदान बनै, अति ही रन मै तब जूझ मरों।");
            fallback.setAuthor("Guru Gobind Singh");
            return fallback;
        }
    }
}
