package com.example.sisproject.dto;

import jakarta.validation.constraints.*;

public record BookRequest(

        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title must be at most 100 characters")
        String title,

        @NotBlank(message = "Author is required")
        String author,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        Double price
) {}