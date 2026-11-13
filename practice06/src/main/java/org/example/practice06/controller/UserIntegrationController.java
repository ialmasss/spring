package org.example.practice06.controller;

import org.example.practice06.dto.ExternalUserDto;
import org.example.practice06.service.ExternalUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "External Users API", description = "Endpoints demonstrating external HTTP calls with timeout and fallback")
public class UserIntegrationController {

    private final ExternalUserService externalUserService;

    public UserIntegrationController(ExternalUserService externalUserService) {
        this.externalUserService = externalUserService;
    }

    @Operation(
            summary = "Get external user by ID",
            description = "Fetches user info from JSONPlaceholder API with a 2-second timeout. Returns a fallback response if the external service times out or fails."
    )
    @ApiResponse(responseCode = "200", description = "User fetched successfully (either from external API or fallback)")
    @GetMapping("/{id}")
    public ResponseEntity<ExternalUserDto> getUser(
            @Parameter(description = "ID of the user (1-10 exist in external API)", example = "1")
            @PathVariable Long id,

            @Parameter(description = "Set true to call a slow endpoint (5s delay) and trigger timeout fallback", example = "false")
            @RequestParam(defaultValue = "false") boolean simulateTimeout
    ) {
        ExternalUserDto user = externalUserService.getUserById(id, simulateTimeout);
        return ResponseEntity.ok(user);
    }
}