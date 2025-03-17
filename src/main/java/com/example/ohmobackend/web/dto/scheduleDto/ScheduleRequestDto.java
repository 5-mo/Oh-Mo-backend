package com.example.ohmobackend.web.dto.scheduleDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ScheduleRequestDto {

    @Getter
    public static class RoutineRequestDto {
        private Long categoryId;

        @Schema(type = "string")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:MM", timezone = "Asia/Seoul")
        private LocalTime time;
        private Boolean alarm;
        private String content;
        private LocalDate endDate;

        @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
        private List<DayOfWeek> routineWeek;
    }
}
