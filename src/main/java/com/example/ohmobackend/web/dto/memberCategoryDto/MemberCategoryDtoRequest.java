package com.example.ohmobackend.web.dto.memberCategoryDto;

import com.example.ohmobackend.domain.enums.ScheduleType;
import lombok.Builder;
import lombok.Getter;

public class MemberCategoryDtoRequest {

    @Getter
    @Builder
    public static class addCategoryRequest {
        private String categoryName;
        private String color;
        private ScheduleType scheduleType;
    }
}
