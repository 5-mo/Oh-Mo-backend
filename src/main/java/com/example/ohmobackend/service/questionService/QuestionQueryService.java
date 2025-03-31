package com.example.ohmobackend.service.questionService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.questionDto.QuestionResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface QuestionQueryService {

    public List<QuestionResponseDto.QuestionDto> getQuestions(Member member);
    public List<QuestionResponseDto.QuestionWithAnswerResponseDto> getQuestionsWithAnswers(Member member, LocalDate date);
}
