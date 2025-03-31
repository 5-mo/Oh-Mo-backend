package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.Question;
import com.example.ohmobackend.web.dto.answerDto.AnswerResponseDto;
import com.example.ohmobackend.web.dto.questionDto.QuestionRequestDto;
import com.example.ohmobackend.web.dto.questionDto.QuestionResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionConverter {

    public static Question questionDtoToEntity(QuestionRequestDto.QuestionRegisterDto dto, Member member) {
        return Question.builder()
                .questionContent(dto.getQuestionContent())
                .emoji(dto.getEmoji())
                .member(member)
                .build();
    }

    public static QuestionResponseDto.QuestionDto toQuestionResponseDto(Question question) {
        List<AnswerResponseDto.AnswerDto> answerDtoList =
                (question.getAnswerList().isEmpty())
                        ? null
                        : question.getAnswerList().stream()
                        .map(AnswerConverter::toAnswerResponseDto)
                        .collect(Collectors.toList());

        return QuestionResponseDto.QuestionDto.builder()
                .id(question.getId())
                .questionContent(question.getQuestionContent())
                .emoji(question.getEmoji())
                .answerList(answerDtoList)
                .build();
    }
}

