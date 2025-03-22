package com.example.ohmobackend.web.dto.scheduleDto;

import com.example.ohmobackend.domain.MemberCategory;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.security.JwtToken;
import com.example.ohmobackend.web.dto.memberCategoryDto.MemberCategoryResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleDto {
        private Long scheduleId;
        private LocalDate date;
        private LocalTime time;
        private boolean alarm;
        private String content;
        private boolean status;
        private ScheduleType scheduleType;
        private MemberCategoryResponseDto.CategoryResponseDto category;
    }
}
