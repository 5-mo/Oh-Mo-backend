package com.example.ohmobackend.service.assigneeService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.MemberAssigneeDto.MemberAssigneeResponseDto;

import java.util.List;

public interface AssigneeQueryService {

    public List<MemberAssigneeResponseDto.MemberAssigneeInfoResponseDto> getTodoScheduleAssignee(Long todoId, Member member);

    public List<MemberAssigneeResponseDto.MemberAssigneeInfoResponseDto> getRoutineScheduleAssignee(Long routineId, Member member);
}
