package com.example.sis3.kafka;

import com.example.sis3.dto.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    @Value("${app.kafka.topic.orders}")
    private String ordersTopic;

    public void publishOrderCreated(OrderCreatedEvent event) {
        log.info("[KAFKA-PRODUCER] Publishing event to topic '{}': key={}, payload={}",
                ordersTopic, event.orderId(), event);

        kafkaTemplate.send(ordersTopic, event.orderId(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("[KAFKA-PRODUCER] Failed to send event for order {}", event.orderId(), ex);
                    } else {
                        log.info("[KAFKA-PRODUCER] Sent successfully to partition {} with offset {}",
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}