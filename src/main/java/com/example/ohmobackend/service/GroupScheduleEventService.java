package com.example.ohmobackend.service;

import com.example.ohmobackend.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupScheduleEventService {

    private final EmitterRepository emitterRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void notifyScheduleChange(ScheduleChangeEvent event) {
        var emitters = emitterRepository.findAllByGroupId(event.getGroupId());

        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("scheduleUpdate")
                        .data(Map.of(
                                "groupId", event.getGroupId(),
                                "date", event.getDate(),
                                "eventType", event.getEventType()
                        )));
            } catch (IOException e) {
                log.error("SSE 이벤트 전송 실패 groupId={}, emitter 제거", event.getGroupId(), e);
                emitterRepository.delete(event.getGroupId(), emitter);
            }
        });
    }
}
