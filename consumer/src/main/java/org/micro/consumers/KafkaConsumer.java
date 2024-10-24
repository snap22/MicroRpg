package org.micro.consumers;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Component
public class KafkaConsumer {

    private final Sinks.Many<ServerSentEvent<String>> sink = Sinks.many().multicast().onBackpressureBuffer();

    @KafkaListener(topics = "${spring.kafka.topic.my-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(
            @Payload GenericRecord message,
            @Header("eventType") String eventType,
            @Header("eventTimestamp") String eventTimestamp
    ) {
        log.info("Consumed event type {} with message {} ", eventType, message);

        ServerSentEvent<String> event = ServerSentEvent.<String>builder()
                .id(UUID.randomUUID().toString())
                .event(eventType)
                .data(message.toString())
                .build();

        Sinks.EmitResult result = sink.tryEmitNext(event);
        if (result.isFailure()) {
            // Handle the error, e.g., log it or take some corrective action
            log.error("Failed to emit message: {} due to: {}", message.toString(), result);
        }
    }

    public Flux<ServerSentEvent<String>> getEventStream() {
        return sink.asFlux()
                .doOnSubscribe(subscription -> log.info("Client connected."))
                .doOnCancel(() -> log.info("Client disconnected."));

    }
}
