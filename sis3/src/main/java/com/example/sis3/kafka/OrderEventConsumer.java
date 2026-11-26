package com.example.sis3.kafka;

import com.example.sis3.dto.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderEventConsumer {

    @KafkaListener(topics = "${app.kafka.topic.orders}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeOrderCreated(
            OrderCreatedEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset
    ) {
        log.info("[KAFKA-CONSUMER] Received event from partition={}, offset={}: {}", partition, offset, event);

        if (event.simulateFailure()) {
            log.error("[KAFKA-CONSUMER] Simulated processing failure for order {}. Triggering retry -> DLT...",
                    event.orderId());
            throw new IllegalStateException("Simulated consumer error for orderId=" + event.orderId());
        }

        log.info("[KAFKA-CONSUMER] Order {} processed and fulfilled successfully.", event.orderId());
    }

    @KafkaListener(topics = "${app.kafka.topic.orders-dlt}", groupId = "order-dlt-group")
    public void consumeDeadLetter(
            OrderCreatedEvent failedEvent,
            @Header(name = KafkaHeaders.DLT_EXCEPTION_MESSAGE, required = false) byte[] exceptionMessage
    ) {
        String reason = exceptionMessage != null ? new String(exceptionMessage) : "Unknown error";
        log.warn("[KAFKA-DLT] Received failed message in DLT! OrderId={}, Reason={}",
                failedEvent.orderId(), reason);
    }
}