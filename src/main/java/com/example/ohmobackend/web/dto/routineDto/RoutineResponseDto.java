package com.example.ohmobackend.web.dto.routineDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class RoutineResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class routineDto {
        private Long routineId;
        private LocalDate date;
        private boolean status;
        private DayOfWeek week;
    }
}
