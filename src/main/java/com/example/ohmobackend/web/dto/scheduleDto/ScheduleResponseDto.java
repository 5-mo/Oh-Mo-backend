package com.example.ohmobackend.web.dto.scheduleDto;

import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.web.dto.memberCategoryDto.MemberCategoryResponseDto;
import com.example.ohmobackend.web.dto.routineDto.RoutineResponseDto;
import com.example.ohmobackend.web.dto.todoDto.TodoRequestDto;
import com.example.ohmobackend.web.dto.todoDto.TodoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ScheduleResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleDto {
        List<ScheduleResponseDto.ScheduleTodoDto> todoList;
        List<ScheduleResponseDto.ScheduleWithRoutineListDto> routineList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleTodoDto {
        private Long scheduleId;
        private LocalDate date;
        private LocalTime time;
        private LocalTime alarmTime;
        private String content;
        private ScheduleType scheduleType;
        private MemberCategoryResponseDto.CategoryResponseDto category;
        private TodoResponseDto.TodoDto todo;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleRoutineDto {
        private Long scheduleId;
        private LocalDate date;
        private LocalTime time;
        private LocalTime alarmTime;
        private String content;
        private ScheduleType scheduleType;
        private MemberCategoryResponseDto.CategoryResponseDto category;
        private RoutineResponseDto.routineDto routine;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleWithRoutineListDto {
        private Long scheduleId;
        private LocalDate date;
        private LocalTime time;
        private LocalTime alarmTime;
        private String content;
        private ScheduleType scheduleType;
        private MemberCategoryResponseDto.CategoryResponseDto category;
        private List<RoutineResponseDto.routineDto> routineList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleByMonthDto {
        private LocalDate date;
        private List<MemberCategoryResponseDto.CategoryResponseDto> categoryList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoutineStatusByWeekDto {
        private String content;
        private List<RoutineResponseDto.routineDto> routineDtoList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleCompletionRateByMonthDto {
        private LocalDate date;
        private double rate;
    }
}
