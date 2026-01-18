package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.Routine;
import com.example.ohmobackend.domain.ScheduleAssignee;
import com.example.ohmobackend.domain.Todo;
import com.example.ohmobackend.web.dto.MemberAssigneeDto.MemberAssigneeResponseDto;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class GroupScheduleConverter {

    public static GroupScheduleResponseDto.GroupTodoWithAssigneeDto toGroupTodoWithAssigneeDto(Todo todo, List<ScheduleAssignee> scheduleAssignees) {
        List<MemberAssigneeResponseDto.MemberAssigneeInfoResponseDto> memberGroupInfos = scheduleAssignees.stream().map(
                scheduleAssignee -> GroupConverter.toAssigneeDto(scheduleAssignee)).collect(Collectors.toList());

        return GroupScheduleResponseDto.GroupTodoWithAssigneeDto.builder()
                .todo(TodoConverter.toTodoDto(todo))
                .memberGroupInfos(memberGroupInfos)
                .build();
    }

    public static GroupScheduleResponseDto.GroupRoutineWithAssigneeDto toGroupRoutineWithAssigneeDto(Routine routine, List<ScheduleAssignee> scheduleAssignees) {
        List<MemberAssigneeResponseDto.MemberAssigneeInfoResponseDto> memberGroupInfos = scheduleAssignees.stream().map(
                scheduleAssignee -> GroupConverter.toAssigneeDto(scheduleAssignee)).collect(Collectors.toList());

        return GroupScheduleResponseDto.GroupRoutineWithAssigneeDto.builder()
                .routine(RoutineConverter.toRoutineDto(routine))
                .memberGroupInfos(memberGroupInfos)
                .build();
    }
}
