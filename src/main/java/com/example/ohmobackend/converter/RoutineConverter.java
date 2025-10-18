package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.Routine;
import com.example.ohmobackend.domain.Schedule;
import com.example.ohmobackend.web.dto.routineDto.RoutineResponseDto;

import java.time.LocalDate;

public class RoutineConverter {

    public static Routine toEntity(Schedule schedule, LocalDate date) {
        return Routine.builder()
                .date(date)
                .status(false)
                .week(date.getDayOfWeek())
                .schedule(schedule)
                .build();
    }

    public static RoutineResponseDto.routineDto toRoutineDto(Routine routine) {
        return RoutineResponseDto.routineDto.builder()
                .routineId(routine.getId())
                .date(routine.getDate())
                .status(routine.isStatus())
                .week(routine.getWeek())
                .build();
    }
}
