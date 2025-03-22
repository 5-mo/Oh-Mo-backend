package com.example.ohmobackend.service.scheduleService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleQueryService {

    public List<ScheduleResponseDto.ScheduleDto> getScheduleList(LocalDate date, Member member, ScheduleType scheduleType);
}
