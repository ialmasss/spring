package com.example.sis3.controller;

import com.example.sis3.dto.CreateOrderRequest;
import com.example.sis3.dto.OrderResponse;
import com.example.sis3.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Orders API", description = "Endpoints for creating, retrieving, and cancelling orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(
            summary = "Create a new order",
            description = "Fetches price via resilient RestClient (with retry & fallback), audits via AOP, and publishes an OrderCreatedEvent to Kafka"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created and event published"),
            @ApiResponse(responseCode = "400", description = "Invalid input payload")
    })
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    @GetMapping("/{orderId}")
    @Operation(
            summary = "Get order by ID",
            description = "Returns order details. Subsequent calls hit Spring Cache (@Cacheable) and bypass storage lookup"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderResponse> getOrder(
            @Parameter(description = "Order identifier", example = "ORD-A1B2C3D4")
            @PathVariable String orderId
    ) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @DeleteMapping("/{orderId}")
    @Operation(
            summary = "Cancel an order",
            description = "Marks order as CANCELLED and evicts stale entry from cache (@CacheEvict)"
    )
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleNotFound(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}