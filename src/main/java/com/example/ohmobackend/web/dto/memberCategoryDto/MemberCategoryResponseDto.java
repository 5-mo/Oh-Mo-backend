package com.example.ohmobackend.web.dto.memberCategoryDto;

import com.example.ohmobackend.domain.enums.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberCategoryResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class addCategoryResponseDto {
        private Long id;
        private String categoryName;
        private String color;
        private ScheduleType scheduleType;
    }
}
