package com.example.ohmobackend.service;

import com.example.ohmobackend.repository.EmitterRepository;
import com.example.ohmobackend.service.scheduleService.GroupScheduleQueryService;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupScheduleEventService {
    private final EmitterRepository emitterRepository;
    private final GroupScheduleQueryService queryService;

    public void notifyScheduleChange(Long groupId, LocalDate date) {
        var emitters = emitterRepository.findAllByGroupId(groupId);

        // 변경된 최신 일정 목록 조회
        GroupScheduleResponseDto.GroupSchedulesDto updatedSchedules = queryService.getScheduleList(groupId, date, null);

        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("scheduleUpdate")
                        .data(updatedSchedules));
            } catch (IOException e) {
                log.error("Error sending SSE", e);
            }
        });
    }
}