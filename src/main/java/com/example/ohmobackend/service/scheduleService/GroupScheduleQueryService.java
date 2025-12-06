package com.example.ohmobackend.service.scheduleService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.groupDto.GroupResponseDto;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleResponseDto;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface GroupScheduleQueryService {

    public ScheduleResponseDto.ScheduleDto getScheduleList(Long groupId, LocalDate date, Member member);

    public List<GroupResponseDto.MemberDto> getTodoScheduleAssignee(Long scheduleId, Member member);

    public List<GroupResponseDto.MemberDto> getRoutineScheduleAssignee(Long routineId, Member member);
}
