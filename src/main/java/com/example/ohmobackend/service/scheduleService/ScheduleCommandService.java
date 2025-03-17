package com.example.ohmobackend.service.scheduleService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleRequestDto;

public interface ScheduleCommandService {

    public void addRoutine(ScheduleRequestDto.RoutineRequestDto requestDto, Member member);
}
