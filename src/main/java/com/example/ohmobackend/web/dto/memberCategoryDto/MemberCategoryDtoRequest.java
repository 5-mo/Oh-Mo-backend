package com.example.ohmobackend.web.dto.memberCategoryDto;

import lombok.Getter;

public class MemberCategoryDtoRequest {

    @Getter
    public static class addCategoryRequest {
        private String categoryName;
        private String color;
        private String scheduleType;
    }
}
