package com.example.ohmobackend.service.scheduleService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleResponseDto;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleResponseDto;

import java.time.LocalDate;

public interface GroupScheduleQueryService {

    public GroupScheduleResponseDto.GroupSchedulesDto getScheduleList(Long groupId, LocalDate date, Member member);
}
