package com.example.ohmobackend.web.dto.scheduleDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ScheduleRequestDto {

    @Getter
    public static class AddRequestDto {
        private Long categoryId;

        @Schema(type = "string")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:MM", timezone = "Asia/Seoul")
        private LocalTime time;
        @Schema(type = "string")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:MM", timezone = "Asia/Seoul")
        private LocalTime alarmTime;
        @NotEmpty(message = "내용은 필수입니다.")
        private String content;
        @NotNull(message = "날짜는 필수입니다. 루틴은 endDate, 투두는 날짜")
        private LocalDate date;
        @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
        private List<DayOfWeek> routineWeek;
    }

    @Getter
    public static class UpdateTodoDateRequestDto {
        private Long scheduleId;
        private LocalDate date;
    }

    @Getter
    public static class UpdateScheduleAlarmTimeDto {
        private Long scheduleId;
        private LocalTime alarmTime;
    }
}
