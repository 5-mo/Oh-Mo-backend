package com.example.ohmobackend.web.dto.questionDto;

import com.example.ohmobackend.web.dto.answerDto.AnswerResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class QuestionResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionDto{
        private Long id;
        private String questionContent;
        private String emoji;
        private List<AnswerResponseDto.AnswerDto> answerList;
    }
}
