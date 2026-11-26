package com.example.sis3.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalPricingClient {

    private final RestClient pricingRestClient;

    public record PriceQuote(BigDecimal unitPrice, String source) {}

    @Retryable(
            retryFor = RestClientException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 500, multiplier = 2.0)
    )
    public PriceQuote fetchUnitPrice(String productCode) {
        log.info("[REST-CLIENT] Calling external pricing service for product: {}", productCode);
        BigDecimal price = pricingRestClient.get()
                .uri("/{code}", productCode)
                .retrieve()
                .body(BigDecimal.class);

        return new PriceQuote(price != null ? price : BigDecimal.valueOf(100), "EXTERNAL_API");
    }

    @Recover
    public PriceQuote fallbackPrice(RestClientException ex, String productCode) {
        log.warn("[REST-CLIENT FALLBACK] External pricing unavailable for {} ({}). Using fallback price.",
                productCode, ex.getMessage());
        return new PriceQuote(BigDecimal.valueOf(99.99), "FALLBACK_DEFAULT");
    }
}