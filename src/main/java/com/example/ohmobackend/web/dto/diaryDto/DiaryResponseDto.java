package com.example.ohmobackend.web.dto.diaryDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class DiaryResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddDiaryResponseDto{
        private Long id;
        private LocalDate date;
        private String content;
    }
}
