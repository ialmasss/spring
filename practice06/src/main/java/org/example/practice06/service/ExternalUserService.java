package org.example.practice06.service;

import org.example.practice06.dto.ExternalUserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ExternalUserService {

    private static final Logger log = LoggerFactory.getLogger(ExternalUserService.class);
    private final RestClient restClient;

    public ExternalUserService(RestClient restClient) {
        this.restClient = restClient;
    }

    public ExternalUserDto getUserById(Long id, boolean simulateSlowServer) {
        String url = simulateSlowServer
                ? "https://httpbin.org/delay/5"
                : "https://jsonplaceholder.typicode.com/users/" + id;

        try {
            log.info("Calling external service: {}", url);

            ExternalUserDto response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(ExternalUserDto.class);

            if (response == null || response.name() == null) {
                return fallbackUser(id, "Empty response body");
            }

            return new ExternalUserDto(
                    response.id(),
                    response.name(),
                    response.email(),
                    "EXTERNAL_API"
            );

        } catch (Exception ex) {
            log.warn("External call failed for user id={}. Reason: {}. Triggering fallback.", id, ex.getMessage());
            return fallbackUser(id, ex.getClass().getSimpleName());
        }
    }

    private ExternalUserDto fallbackUser(Long id, String reason) {
        return new ExternalUserDto(
                id,
                "Default Fallback User (Reason: " + reason + ")",
                "fallback@local.service",
                "FALLBACK"
        );
    }
}