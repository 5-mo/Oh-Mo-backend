package com.example.ohmobackend.service.scheduleService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleRequestDto;
import com.example.ohmobackend.web.dto.routineDto.RoutineResponseDto;
import com.example.ohmobackend.web.dto.todoDto.TodoResponseDto;

import java.util.List;

public interface GroupScheduleCommandService {
    List<RoutineResponseDto.RoutineDto> addGroupRoutine(GroupScheduleRequestDto.GroupScheduleAddRequestDto request, Member member);

    TodoResponseDto.TodoDto addGroupTodo(GroupScheduleRequestDto.GroupScheduleAddRequestDto request, Member member);

    TodoResponseDto.TodoDto nlpAddGroupTodo(GroupScheduleRequestDto.GroupNlpAddRequestDto request, Member member);

    TodoResponseDto.TodoDto updateGroupTodo(Long todoId, GroupScheduleRequestDto.GroupTodoUpdateRequestDto request, Member member);

    void deleteGroupTodo(Long todoId, Member member);

    List<RoutineResponseDto.RoutineDto> updateGroupRoutine(Long scheduleId, GroupScheduleRequestDto.GroupRoutineUpdateRequestDto request, Member member);

    void deleteGroupRoutine(Long routineId, Member member);
}
