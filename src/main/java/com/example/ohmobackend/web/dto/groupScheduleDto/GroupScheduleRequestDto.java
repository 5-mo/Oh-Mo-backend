package com.example.ohmobackend.web.dto.groupScheduleDto;

import
        com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public class GroupScheduleRequestDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GroupScheduleAddRequestDto {
        private Long groupId;

        @Schema(type = "string")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:MM", timezone = "Asia/Seoul")
        private LocalTime time;
        @Schema(type = "string")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:MM", timezone = "Asia/Seoul")
        private LocalTime alarmTime;
        private String content;
        private LocalDate date;

        @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
        private Set<DayOfWeek> routineWeek;
    }

    @Getter
    public static class TodoScheduleAssigneeRequestDto {
        private Long todoId;
        private Long memberGroupId;
    }

    @Getter
    public static class RoutineScheduleAssigneeRequestDto {
        private Long routineId;
        private Long memberGroupId;
    }
}
