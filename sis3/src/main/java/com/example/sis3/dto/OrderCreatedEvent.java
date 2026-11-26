package com.example.sis3.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderCreatedEvent(
        String orderId,
        String productCode,
        int quantity,
        BigDecimal totalPrice,
        boolean simulateFailure,
        Instant occurredAt
) {}