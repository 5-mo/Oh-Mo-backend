package com.example.ohmobackend.web.dto.groupScheduleDto;

import com.example.ohmobackend.web.dto.groupDto.GroupResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class GroupScheduleResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TodoScheduleAssigneeDto {
        private Long todoId;
        private List<GroupResponseDto.MemberDto> memberDtoList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoutineScheduleAssigneeDto {
        private Long routineId;
        private List<GroupResponseDto.MemberDto> memberDtoList;
    }
}
