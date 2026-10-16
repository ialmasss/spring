package com.example.practice04.dto;

import java.math.BigDecimal;

public record BookSummaryResponse(Long id, String title, BigDecimal price) {
}
