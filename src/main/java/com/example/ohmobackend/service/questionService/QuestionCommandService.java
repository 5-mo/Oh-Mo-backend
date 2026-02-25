package com.example.ohmobackend.service.questionService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.questionDto.QuestionRequestDto;
import com.example.ohmobackend.web.dto.questionDto.QuestionResponseDto;

public interface QuestionCommandService {

    public void addQuestion(Member member, QuestionRequestDto.QuestionRegisterDto request);

    public QuestionResponseDto.QuestionDto updateQuestion(Long questionId, QuestionRequestDto.QuestionUpdateDto request, Member member);

    public void deleteQuestion(Long questionId, Member member);
}
