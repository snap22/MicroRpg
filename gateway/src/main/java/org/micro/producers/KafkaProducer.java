package org.micro.producers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.micro.avro.QuestEvent;
import org.micro.enums.QuestEventType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, QuestEvent> kafkaTemplate;

    @Value("${spring.kafka.topic.my-topic}")
    private String topic;

    /**
     * Produce an enriched message to kafka topic
     * @param eventType type of the event
     * @param questEvent payload of the message
     */
    public void sendQuestEvent(QuestEventType eventType, QuestEvent questEvent) {
        String key = String.format("QP-%d", questEvent.getPlayerId());

        Message<QuestEvent> message = MessageBuilder
                .withPayload(questEvent)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.KEY, key)
                .setHeader("eventType", eventType.getEventTypeName().getBytes(StandardCharsets.UTF_8))
                .setHeader("eventTimestamp", Instant.now().toString().getBytes(StandardCharsets.UTF_8))
                .build();

        kafkaTemplate.send(message);

        log.info("Sent event to {} : {}", topic, questEvent);
    }
}