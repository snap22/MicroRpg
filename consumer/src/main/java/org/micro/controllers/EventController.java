package org.micro.controllers;

import lombok.RequiredArgsConstructor;
import org.micro.consumers.KafkaConsumer;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class EventController {
    private final KafkaConsumer consumer;

    @GetMapping(value = "/sse/quests", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamKafkaEvents(ServerHttpResponse response) {
        return consumer.getEventStream();
    }
}
