package com.example.ohmobackend.service.scheduleService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleQueryService {

    public List<ScheduleResponseDto.ScheduleDto> getScheduleList(LocalDate date, Member member, ScheduleType scheduleType);

//    public List<ScheduleResponseDto.ScheduleDto> getCompleteTodoList(LocalDate date, Member member);

    public List<ScheduleResponseDto.ScheduleByMonthDto> getScheduleListByMonth(String yearMonth, Member member);

    public List<ScheduleResponseDto.ScheduleDto> getScheduleListByKeyword(String keyword, Member member);

    public List<ScheduleResponseDto.RoutineStatusByContentDto> getRoutineStatusList(LocalDate startDate, LocalDate endDate, Member member);

//    public List<ScheduleResponseDto.ScheduleCompletionRateByMonthDto> getScheduleCompletionReteByMonth(String yearMonth,  Member member);
}
