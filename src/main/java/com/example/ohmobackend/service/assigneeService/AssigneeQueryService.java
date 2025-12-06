package com.example.ohmobackend.service.assigneeService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.groupDto.GroupResponseDto;

import java.util.List;

public interface AssigneeQueryService {

    public List<GroupResponseDto.MemberDto> getTodoScheduleAssignee(Long todoId, Member member);

    public List<GroupResponseDto.MemberDto> getRoutineScheduleAssignee(Long routineId, Member member);
}
