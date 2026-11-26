package com.example.sis3.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload to create a new order")
public record CreateOrderRequest(
        @Schema(description = "Product SKU code", example = "LAPTOP-PRO-16")
        String productCode,

        @Schema(description = "Quantity of items", example = "2")
        int quantity,

        @Schema(description = "Set true to simulate consumer failure and test DLT routing", example = "false")
        boolean simulateFailure
) {}