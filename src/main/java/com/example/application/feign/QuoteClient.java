package com.example.application.feign;

import com.example.application.dto.response.QuoteApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "quote-service", url = "${quote.service.url}")
public interface QuoteClient {

    @GetMapping("/random")
    QuoteApiResponse getRandomQuote();
}
