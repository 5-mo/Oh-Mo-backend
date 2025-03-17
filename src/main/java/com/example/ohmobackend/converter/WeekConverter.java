package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.RoutineWeek;
import com.example.ohmobackend.domain.Schedule;

import java.time.DayOfWeek;

public class WeekConverter {

    public RoutineWeek toEntity(Schedule schedule, DayOfWeek week) {
        return RoutineWeek.builder()
                .week(week)
                .build();
    }
}
