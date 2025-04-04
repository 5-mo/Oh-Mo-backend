package com.example.ohmobackend.web.dto.dayLogDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class DayLogResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddDayLogResponseDto{
        private Long id;
        private LocalDate date;
        private String emoji;
    }
}
