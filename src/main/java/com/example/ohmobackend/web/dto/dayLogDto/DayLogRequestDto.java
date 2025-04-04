package com.example.ohmobackend.web.dto.dayLogDto;

import lombok.Getter;

import java.time.LocalDate;

public class DayLogRequestDto {

    @Getter
    public static class AddDayLogRequestDto {
        private LocalDate date;
        private String emoji;
    }
}
