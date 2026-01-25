package com.example.ohmobackend.web.dto.groupScheduleDto;

import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.web.dto.MemberAssigneeDto.MemberAssigneeResponseDto;
import com.example.ohmobackend.web.dto.memberCategoryDto.MemberCategoryResponseDto;
import com.example.ohmobackend.web.dto.routineDto.RoutineResponseDto;
import com.example.ohmobackend.web.dto.todoDto.TodoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class GroupScheduleResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupTodoWithAssigneeDto {
        private TodoResponseDto.TodoDto todo;
        private List<MemberAssigneeResponseDto.MemberAssigneeInfoResponseDto> memberGroupInfos;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupRoutineWithAssigneeDto {
        private RoutineResponseDto.RoutineDto routine;
        private List<MemberAssigneeResponseDto.MemberAssigneeInfoResponseDto> memberGroupInfos;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupScheduleTodoDto {
        private Long scheduleId;
        private LocalDate date;
        private LocalTime time;
        private LocalTime alarmTime;
        private String content;
        private ScheduleType scheduleType;
        private MemberCategoryResponseDto.CategoryResponseDto category;
        private GroupTodoWithAssigneeDto groupTodoWithAssignee;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupScheduleRoutineDto {
        private Long scheduleId;
        private LocalDate date;
        private LocalTime time;
        private LocalTime alarmTime;
        private String content;
        private ScheduleType scheduleType;
        private MemberCategoryResponseDto.CategoryResponseDto category;
        private GroupRoutineWithAssigneeDto groupRoutineWithAssignee;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupSchedulesDto {
        private List<GroupScheduleTodoDto> todos;
        private List<GroupScheduleRoutineDto> routines;
    }
}
