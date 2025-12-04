package com.example.ohmobackend.web.dto.groupScheduleDto;

import com.example.ohmobackend.domain.enums.ScheduleType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class GroupScheduleRequestDto {

    @Getter
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
        private List<DayOfWeek> routineWeek;
    }

    @Getter
    public static class ScheduleAssigneeDto {
        private Long scheduleId;
        private List<Long> memberIdList;
    }
}
