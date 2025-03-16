package com.example.ohmobackend.web.dto.memberCategoryDto;

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
        private String scheduleType;
    }
}
