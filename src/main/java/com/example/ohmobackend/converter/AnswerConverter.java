package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.Answer;
import com.example.ohmobackend.domain.Question;
import com.example.ohmobackend.web.dto.answerDto.AnswerRequestDto;
import com.example.ohmobackend.web.dto.answerDto.AnswerResponseDto;

public class AnswerConverter {

    public static AnswerResponseDto.AnswerDto toAnswerResponseDto(Answer answer) {
        return AnswerResponseDto.AnswerDto.builder()
                .id(answer.getId())
                .answer(answer.getAnswer())
                .date(answer.getDate())
                .build();
    }

    public static Answer toEntity(AnswerRequestDto.AddAnswerDto requestDto, Question question) {
        return Answer.builder()
                .answer(requestDto.getAnswer())
                .date(requestDto.getDate())
                .question(question)
                .build();
    }
}
