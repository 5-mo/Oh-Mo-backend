package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.Question;
import com.example.ohmobackend.web.dto.questionDto.QuestionRequestDto;
import com.example.ohmobackend.web.dto.questionDto.QuestionResponseDto;

public class QuestionConverter {

    public static Question questionDtoToEntity(QuestionRequestDto.QuestionRegisterDto dto, Member member) {
        return Question.builder()
                .questionContent(dto.getQuestionContent())
                .emoji(dto.getEmoji())
                .member(member)
                .build();
    }

    public static QuestionResponseDto.QuestionDto toQuestionResponseDto(Question question) {
        return QuestionResponseDto.QuestionDto.builder()
                .id(question.getId())
                .questionContent(question.getQuestionContent())
                .emoji(question.getEmoji())
                .build();
    }
}

