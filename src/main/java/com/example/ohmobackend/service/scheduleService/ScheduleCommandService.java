package com.example.ohmobackend.service.scheduleService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleRequestDto;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleResponseDto;

public interface ScheduleCommandService {

    public void addRoutine(ScheduleRequestDto.RoutineRequestDto requestDto, Member member);

    public void addTodo(ScheduleRequestDto.TodoRequestDto requestDto, Member member);

    public void updateScheduleStatus(Long scheduleId);

    public ScheduleResponseDto.ScheduleDto updateScheduleDate(ScheduleRequestDto.UpdateTodoDateRequestDto requestDto);

    public void updateScheduleAlarmTime(ScheduleRequestDto.UpdateScheduleAlarmTimeDto requestDto);
}
