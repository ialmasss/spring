package com.example.practice04.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record BookRequest(
        @NotBlank @Size(max = 255) String title,
        @NotBlank @Pattern(regexp = "\\d{13}", message = "ISBN must be 13 digits") String isbn,
        @Min(1000) @Max(2100) Integer year,
        @NotNull @Positive BigDecimal price,
        String description,
        @NotNull Long authorId,
        @NotNull Long genreId) {
}
