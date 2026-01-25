package com.example.ohmobackend.service.scheduleService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleQueryService {

    public ScheduleResponseDto.ScheduleByDateDto getScheduleList(LocalDate date, Member member);

    public List<ScheduleResponseDto.ScheduleTodoDto> getCompleteTodoList(LocalDate date, Member member);

    public List<ScheduleResponseDto.ScheduleByMonthDto> getScheduleListByMonth(String yearMonth, Member member);

    public ScheduleResponseDto.ScheduleByKeyWordDto getScheduleListByKeyword(String keyword, Member member);

    public List<ScheduleResponseDto.ScheduleWithRoutineListDto> getRoutineStatusList(LocalDate startDate, LocalDate endDate, Member member);

    public List<ScheduleResponseDto.ScheduleCompletionRateByMonthDto> getScheduleCompletionReteByMonth(String yearMonth,  Member member);
}
