package com.example.ohmobackend.service.scheduleService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleRequestDto;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleResponseDto;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleResponseDto;

import java.time.LocalDate;

public interface GroupScheduleQueryService {

    public ScheduleResponseDto.ScheduleDto getScheduleList(Long groupId, LocalDate date, Member member);

    public GroupScheduleResponseDto.ScheduleAssigneeDto getScheduleAssignee(Long scheduleId, Member member);
}
