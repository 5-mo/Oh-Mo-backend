package com.example.ohmobackend.web.dto.memberCategoryDto;

import com.example.ohmobackend.domain.enums.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberCategoryDtoRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class addCategoryRequest {
        private String categoryName;
        private String color;
        private ScheduleType scheduleType;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateCategoryRequest {
        private String categoryName;
        private String color;
    }
}
