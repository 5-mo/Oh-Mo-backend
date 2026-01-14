package com.example.ohmobackend.service.dayLogService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.dayLogDto.DayLogResponseDto;

import java.time.LocalDate;

public interface DayLogQueryService {

    public DayLogResponseDto.AddDayLogResponseDto getDayLogEmoji(Member member, LocalDate date);
}
