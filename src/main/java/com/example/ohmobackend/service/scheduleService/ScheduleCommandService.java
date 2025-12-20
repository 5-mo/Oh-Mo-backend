package com.example.ohmobackend.service.scheduleService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.routineDto.RoutineResponseDto;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleRequestDto;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleResponseDto;
import com.example.ohmobackend.web.dto.todoDto.TodoResponseDto;

import java.util.List;

public interface ScheduleCommandService {

    public List<RoutineResponseDto.RoutineDto> addRoutine(ScheduleRequestDto.AddRequestDto requestDto, Member member);

    public List<RoutineResponseDto.RoutineDto> updateRoutine(Long scheduleId, ScheduleRequestDto.AddRequestDto requestDto, Member member);

    public TodoResponseDto.TodoDto addTodo(ScheduleRequestDto.AddRequestDto requestDto, Member member);

    public TodoResponseDto.TodoDto updateTodo(Long scheduleId, ScheduleRequestDto.AddRequestDto requestDto, Member member);

    public ScheduleResponseDto.ScheduleTodoDto updateScheduleDate(ScheduleRequestDto.UpdateTodoDateRequestDto requestDto, Member member);

    public void updateScheduleAlarmTime(ScheduleRequestDto.UpdateScheduleAlarmTimeDto requestDto, Member member);
}
