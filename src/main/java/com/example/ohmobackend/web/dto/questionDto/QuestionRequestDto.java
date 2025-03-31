package com.example.ohmobackend.web.dto.questionDto;

import lombok.Getter;

public class QuestionRequestDto {

    @Getter
    public static class QuestionRegisterDto {
        private String questionContent;
        private String emoji;
    }
}
