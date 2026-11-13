package org.example.practice06.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User profile fetched from an external service or fallback")
public record ExternalUserDto(
        @Schema(description = "User ID", example = "1")
        Long id,

        @Schema(description = "Full name", example = "Leanne Graham")
        String name,

        @Schema(description = "Email address", example = "Sincere@april.biz")
        String email,

        @Schema(description = "Data source: EXTERNAL_API or FALLBACK", example = "EXTERNAL_API")
        String source
) {
}