package com.example.ohmobackend.service.assigneeService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleRequestDto;

public interface AssigneeCommandService {

    public void addTodoScheduleAssignee(GroupScheduleRequestDto.TodoScheduleAssigneeRequestDto requestDto, Member member);

    public void addRoutineScheduleAssignee(GroupScheduleRequestDto.RoutineScheduleAssigneeRequestDto requestDto, Member member);

    public void updateAssigneeStatus(Long assigneeId, Member member);
}
