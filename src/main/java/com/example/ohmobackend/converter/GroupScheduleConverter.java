package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.MemberGroup;
import com.example.ohmobackend.domain.Routine;
import com.example.ohmobackend.domain.Todo;
import com.example.ohmobackend.web.dto.groupDto.GroupResponseDto;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class GroupScheduleConverter {

    public static GroupScheduleResponseDto.TodoScheduleAssigneeDto toTodoScheduleAssigneeDto(Todo todo, List<MemberGroup> memberGroupList) {
        List<GroupResponseDto.MemberDto> memberDtoList = memberGroupList.stream()
                .map(memberGroup -> GroupConverter.toMemberDto(memberGroup.getMember(), memberGroup.getNickname()))
                .collect(Collectors.toList());

        return GroupScheduleResponseDto.TodoScheduleAssigneeDto.builder()
                .todoId(todo.getId())
                .memberDtoList(memberDtoList)
                .build();
    }

    public static GroupScheduleResponseDto.RoutineScheduleAssigneeDto toRoutineScheduleAssigneeDto(Routine routine, List<MemberGroup> memberGroupList) {
        List<GroupResponseDto.MemberDto> memberDtoList = memberGroupList.stream()
                .map(memberGroup -> GroupConverter.toMemberDto(memberGroup.getMember(), memberGroup.getNickname()))
                .collect(Collectors.toList());

        return GroupScheduleResponseDto.RoutineScheduleAssigneeDto.builder()
                .routineId(routine.getId())
                .memberDtoList(memberDtoList)
                .build();
    }
}
