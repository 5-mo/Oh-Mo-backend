package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.Routine;
import com.example.ohmobackend.domain.Schedule;

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
}
