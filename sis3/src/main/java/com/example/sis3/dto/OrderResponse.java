package com.example.sis3.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;

@Schema(description = "Order details response")
public record OrderResponse(
        @Schema(description = "Unique order identifier", example = "ORD-1001")
        String orderId,

        @Schema(description = "Product SKU code", example = "LAPTOP-PRO-16")
        String productCode,

        @Schema(description = "Quantity ordered", example = "2")
        int quantity,

        @Schema(description = "Calculated total price", example = "2999.98")
        BigDecimal totalPrice,

        @Schema(description = "Price source (EXTERNAL_API or FALLBACK_CACHE)", example = "FALLBACK_CACHE")
        String priceSource,

        @Schema(description = "Order status", example = "CREATED")
        String status,

        @Schema(description = "Creation timestamp")
        Instant createdAt
) {}