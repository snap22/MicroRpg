package org.micro.controllers;

import lombok.RequiredArgsConstructor;
import org.micro.avro.QuestEvent;
import org.micro.enums.QuestEventType;
import org.micro.producers.KafkaProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final KafkaProducer kafkaProducer;

    /**
     * get all possible quest event types
     * @return quest event types
     */
    @GetMapping("/types/quests")
    @PreAuthorize("hasRole('PRODUCE-EVENTS')")
    public ResponseEntity<QuestEventType[]> getQuestEventTypes() {
        QuestEventType[] questEventTypes = QuestEventType.values();
        return new ResponseEntity<>(questEventTypes, HttpStatus.OK);
    }

    /**
     * Create a new event based on type
     * @param eventType type
     * @param event payload
     * @return info message
     */
    @PostMapping("/quests/{eventType}")
    @PreAuthorize("hasRole('PRODUCE-EVENTS')")
    public ResponseEntity<String> createQuestEvent(
            @PathVariable QuestEventType eventType,
            @RequestBody QuestEvent event
    ) {
        kafkaProducer.sendQuestEvent(eventType, event);
        return new ResponseEntity<>("Event created successfully!", HttpStatus.CREATED);
    }



}
