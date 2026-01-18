package com.example.ohmobackend.web.dto.groupScheduleDto;

import com.example.ohmobackend.web.dto.MemberAssigneeDto.MemberAssigneeResponseDto;
import com.example.ohmobackend.web.dto.routineDto.RoutineResponseDto;
import com.example.ohmobackend.web.dto.todoDto.TodoResponseDto;
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
    public static class GroupTodoWithAssigneeDto{
        private TodoResponseDto.TodoDto todo;
        private List<MemberAssigneeResponseDto.MemberAssigneeInfoResponseDto> memberGroupInfos;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupRoutineWithAssigneeDto{
        private RoutineResponseDto.RoutineDto routine;
        private List<MemberAssigneeResponseDto.MemberAssigneeInfoResponseDto> memberGroupInfos;
    }

}
