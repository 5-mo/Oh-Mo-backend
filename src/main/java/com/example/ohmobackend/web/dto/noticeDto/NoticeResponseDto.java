package com.example.ohmobackend.web.dto.noticeDto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

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

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoticeByMonthDto {
        private LocalDate date;
        private List<NoticeDto> notices;
    }
}
