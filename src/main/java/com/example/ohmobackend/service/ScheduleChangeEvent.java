package com.example.ohmobackend.service;

import com.example.ohmobackend.apiPayload.code.status.ScheduleEventType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class ScheduleChangeEvent {

    private final Long groupId;
    private final LocalDate date;
    private final ScheduleEventType eventType;
}
