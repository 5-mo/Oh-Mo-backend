package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.Daylog;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.dayLogDto.DayLogRequestDto;
import com.example.ohmobackend.web.dto.dayLogDto.DayLogResponseDto;

public class DayLogConverter {

    public static Daylog toEntity(DayLogRequestDto.AddDayLogRequestDto requestDto, Member member) {
        return Daylog.builder()
                .member(member)
                .date(requestDto.getDate())
                .emoji(requestDto.getEmoji())
                .build();
    }

    public static DayLogResponseDto.AddDayLogResponseDto toAddDayLogResponseDto(Daylog daylog) {
        return DayLogResponseDto.AddDayLogResponseDto.builder()
                .id(daylog.getId())
                .date(daylog.getDate())
                .emoji(daylog.getEmoji())
                .build();
    }
}
