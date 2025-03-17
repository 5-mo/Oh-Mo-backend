package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.MemberCategory;
import com.example.ohmobackend.domain.Schedule;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleRequestDto;

import java.time.LocalDate;

public class ScheduleConverter {

    static public Schedule toEntity(
            ScheduleRequestDto.RoutineRequestDto requestDto,
            MemberCategory memberCategory,
            LocalDate date,
            ScheduleType scheduleType) {
        return Schedule.builder()
                .date(date)
                .time(requestDto.getTime())
                .alarm(requestDto.getAlarm())
                .content(requestDto.getContent())
                .status(false)
                .routineEndDate(requestDto.getEndDate())
                .scheduleType(scheduleType)
                .memberCategory(memberCategory)
                .build();
    }

}
