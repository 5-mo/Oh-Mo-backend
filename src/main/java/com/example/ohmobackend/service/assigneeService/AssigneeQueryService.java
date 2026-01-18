package com.example.ohmobackend.service.assigneeService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.MemberAssigneeDto.MemberAssigneeResponseDto;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleResponseDto;

import java.util.List;

public interface AssigneeQueryService {

    public GroupScheduleResponseDto.GroupTodoWithAssigneeDto getTodoScheduleAssignee(Long todoId, Member member);

    public GroupScheduleResponseDto.GroupRoutineWithAssigneeDto getRoutineScheduleAssignee(Long routineId, Member member);
}
