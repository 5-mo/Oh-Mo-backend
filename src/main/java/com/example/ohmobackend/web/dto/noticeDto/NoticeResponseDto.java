package com.example.ohmobackend.web.dto.noticeDto;

import lombok.*;

import java.time.LocalDate;

public class NoticeResponseDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoticeDto {
        private String notice;
        private LocalDate date;
        private Long groupId;
    }
}
