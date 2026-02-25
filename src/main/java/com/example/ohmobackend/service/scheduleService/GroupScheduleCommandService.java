package com.example.ohmobackend.service.scheduleService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleRequestDto;
import com.example.ohmobackend.web.dto.routineDto.RoutineResponseDto;
import com.example.ohmobackend.web.dto.todoDto.TodoResponseDto;

import java.util.List;

public interface GroupScheduleCommandService {
    public List<RoutineResponseDto.RoutineDto> addGroupRoutine(GroupScheduleRequestDto.GroupScheduleAddRequestDto request, Member member);

    public TodoResponseDto.TodoDto addGroupTodo(GroupScheduleRequestDto.GroupScheduleAddRequestDto request, Member member);

}
