package com.example.ohmobackend.service.scheduleService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleRequestDto;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleResponseDto;

public interface ScheduleCommandService {

    public void addRoutine(ScheduleRequestDto.AddRequestDto requestDto, Member member);

    public void addTodo(ScheduleRequestDto.AddRequestDto requestDto, Member member);

    public ScheduleResponseDto.ScheduleTodoDto updateScheduleDate(ScheduleRequestDto.UpdateTodoDateRequestDto requestDto, Member member);

    public void updateScheduleAlarmTime(ScheduleRequestDto.UpdateScheduleAlarmTimeDto requestDto, Member member);

    // 반복되는 요일에 해당하는 날짜들 반환
    void addGroupRoutine(ScheduleRequestDto.GroupRoutineRequestDto requestDto, Member member);

    void addGroupTodo(ScheduleRequestDto.GroupTodoRequestDto requestDto, Member member);

    void addScheduleAssignee(ScheduleRequestDto.ScheduleAssigneeDto requestDto, Member member);
}
