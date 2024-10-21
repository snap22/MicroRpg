package org.micro.controllers;

import org.micro.dto.QuestEventRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class EventController {


    @GetMapping("/types/quests")
    @PreAuthorize("hasRole('PRODUCE-EVENTS')")
    public ResponseEntity<String> getQuestEventTypes() {
        return new ResponseEntity<>("Quest fetched successfully!", HttpStatus.OK);
    }

    @PostMapping("/quests")
    @PreAuthorize("hasRole('PRODUCE-EVENTS')")
    public ResponseEntity<String> createQuestEvent() {
        return new ResponseEntity<>("Quest created successfully!", HttpStatus.CREATED);
    }



}
