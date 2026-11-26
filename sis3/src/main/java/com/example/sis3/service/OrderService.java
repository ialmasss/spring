package com.example.sis3.service;

import com.example.sis3.client.ExternalPricingClient;
import com.example.sis3.dto.CreateOrderRequest;
import com.example.sis3.dto.OrderCreatedEvent;
import com.example.sis3.dto.OrderResponse;
import com.example.sis3.kafka.OrderEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ExternalPricingClient pricingClient;
    private final OrderEventProducer eventProducer;

    private final Map<String, OrderResponse> orderStore = new ConcurrentHashMap<>();

    public OrderResponse createOrder(CreateOrderRequest request) {
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        ExternalPricingClient.PriceQuote quote = pricingClient.fetchUnitPrice(request.productCode());
        BigDecimal total = quote.unitPrice().multiply(BigDecimal.valueOf(request.quantity()));

        OrderResponse response = new OrderResponse(
                orderId,
                request.productCode(),
                request.quantity(),
                total,
                quote.source(),
                "CREATED",
                Instant.now()
        );

        orderStore.put(orderId, response);

        OrderCreatedEvent event = new OrderCreatedEvent(
                orderId,
                request.productCode(),
                request.quantity(),
                total,
                request.simulateFailure(),
                Instant.now()
        );
        eventProducer.publishOrderCreated(event);

        return response;
    }

    @Cacheable(value = "orders", key = "#orderId")
    public OrderResponse getOrderById(String orderId) {
        log.info("[SERVICE-DB] Cache miss! Fetching order {} from storage...", orderId);
        OrderResponse order = orderStore.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
        return order;
    }

    @CacheEvict(value = "orders", key = "#orderId")
    public OrderResponse cancelOrder(String orderId) {
        OrderResponse existing = orderStore.get(orderId);
        if (existing == null) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
        OrderResponse cancelled = new OrderResponse(
                existing.orderId(),
                existing.productCode(),
                existing.quantity(),
                existing.totalPrice(),
                existing.priceSource(),
                "CANCELLED",
                existing.createdAt()
        );
        orderStore.put(orderId, cancelled);
        return cancelled;
    }
}