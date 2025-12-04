package com.example.ohmobackend.web.dto.noticeDto;

import lombok.*;

import java.time.LocalDate;

public class NoticeRequestDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddNoticeDto {
        private String notice;
        private LocalDate date;
        private Long groupId;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatchNoticeDto {
        private String notice;
        private LocalDate date;
    }
}
