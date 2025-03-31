package com.example.ohmobackend.web.dto.answerDto;

import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.web.dto.memberCategoryDto.MemberCategoryResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

public class AnswerResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerDto {
        private Long id;
        private String answer;
        private LocalDate date;
    }
}
