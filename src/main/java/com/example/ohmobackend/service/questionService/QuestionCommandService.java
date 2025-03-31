package com.example.ohmobackend.service.questionService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.questionDto.QuestionRequestDto;

public interface QuestionCommandService {

    public void addQuestion(Member member, QuestionRequestDto.QuestionRegisterDto request);
}
