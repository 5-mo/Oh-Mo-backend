package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.MemberCategory;
import com.example.ohmobackend.domain.Schedule;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleRequestDto;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleResponseDto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleConverter {

    static public Schedule routineToEntity(
            ScheduleRequestDto.RoutineRequestDto requestDto,
            MemberCategory memberCategory,
            LocalDate date) {
        return Schedule.builder()
                .date(date)
                .time(requestDto.getTime())
                .alarm(requestDto.getAlarm())
                .content(requestDto.getContent())
                .status(false)
                .routineEndDate(requestDto.getEndDate())
                .scheduleType(ScheduleType.ROUTINE)
                .memberCategory(memberCategory)
                .build();
    }

    static public Schedule todoToEntity(
            ScheduleRequestDto.TodoRequestDto requestDto,
            MemberCategory memberCategory) {
        return Schedule.builder()
                .date(requestDto.getDate())
                .time(requestDto.getTime())
                .alarm(requestDto.getAlarm())
                .content(requestDto.getContent())
                .status(false)
                .scheduleType(ScheduleType.TO_DO)
                .memberCategory(memberCategory)
                .build();
    }

    static public ScheduleResponseDto.ScheduleDto toScheduleDto(
            Schedule schedule) {
        return ScheduleResponseDto.ScheduleDto.builder()
                .scheduleId(schedule.getId())
                .date(schedule.getDate())
                .time(schedule.getTime() != null ? schedule.getTime() : LocalTime.MIDNIGHT)
                .alarm(schedule.isAlarm())
                .content(schedule.getContent())
                .status(schedule.isStatus())
                .scheduleType(schedule.getScheduleType())
                .category(MemberCategoryConverter.toAddCategoryResponseDto(schedule.getMemberCategory()))
                .build();
    }

}
