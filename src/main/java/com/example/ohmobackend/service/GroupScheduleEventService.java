package com.example.ohmobackend.service;

import com.example.ohmobackend.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupScheduleEventService {
    private final EmitterRepository emitterRepository;

    public void notifyScheduleChange(Long groupId, LocalDate date) {
        var emitters = emitterRepository.findAllByGroupId(groupId);

        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("scheduleUpdate")
                        .data(Map.of(
                                "groupId", groupId,
                                "date", date
                        )));
            } catch (IOException e) {
                log.error("Error sending SSE", e);
            }
        });
    }
}