package com.example.ohmobackend.service.answerService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.answerDto.AnswerRequestDto;
import com.example.ohmobackend.web.dto.answerDto.AnswerResponseDto;

public interface AnswerCommandService {

    public AnswerResponseDto.AnswerDto addAnswer(Member member, AnswerRequestDto.AddAnswerDto requestDto);
}
