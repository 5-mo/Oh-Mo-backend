package com.example.ohmobackend.service.dayLogService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.dayLogDto.DayLogRequestDto;
import com.example.ohmobackend.web.dto.dayLogDto.DayLogResponseDto;

public interface DayLogCommandService {

    public DayLogResponseDto.AddDayLogResponseDto addDayLog(Member member, DayLogRequestDto.AddDayLogRequestDto requestDto);
}
