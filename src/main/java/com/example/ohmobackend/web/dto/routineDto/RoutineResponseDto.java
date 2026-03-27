package com.example.ohmobackend.web.dto.routineDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

public class RoutineResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoutineDto {
        private Long routineId;
        private LocalDate date;
        private boolean status;
        private DayOfWeek week;
        private Set<DayOfWeek> repeatWeek;
    }
}
