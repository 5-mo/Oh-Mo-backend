package com.example.ohmobackend.web.dto.diaryDto;

import lombok.Getter;

import java.time.LocalDate;

public class DiaryRequestDto {

    @Getter
    public static class AddDiaryRequestDto {
        private String content;
        private LocalDate date;
    }
}
