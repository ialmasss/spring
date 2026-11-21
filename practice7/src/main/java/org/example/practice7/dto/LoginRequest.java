package org.example.practice7.dto;

public record LoginRequest(
        String username,
        String password
) {}