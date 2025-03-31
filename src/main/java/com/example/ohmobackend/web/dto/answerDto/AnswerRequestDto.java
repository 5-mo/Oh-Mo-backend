package com.example.ohmobackend.web.dto.answerDto;

import lombok.Getter;

import java.time.LocalDate;

public class AnswerRequestDto {

    @Getter
    public static class AddAnswerDto {
        private Long questionId;
        private String answer;
        private LocalDate date;
    }
}
