package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.Answer;
import com.example.ohmobackend.web.dto.answerDto.AnswerResponseDto;

public class AnswerConverter {

    public static AnswerResponseDto.AnswerDto toAnswerResponseDto(Answer answer) {
        return AnswerResponseDto.AnswerDto.builder()
                .id(answer.getId())
                .answer(answer.getAnswer())
                .date(answer.getDate())
                .build();
    }
}
