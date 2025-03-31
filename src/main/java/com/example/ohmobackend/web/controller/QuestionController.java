package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.questionService.QuestionCommandService;
import com.example.ohmobackend.service.questionService.QuestionQueryService;
import com.example.ohmobackend.web.dto.questionDto.QuestionRequestDto;
import com.example.ohmobackend.web.dto.questionDto.QuestionResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/question")
@Slf4j
public class QuestionController {

    final QuestionCommandService questionCommandService;
    final QuestionQueryService questionQueryService;

    @PostMapping("/")
    @Operation(summary = "질문 등록 API", description = "질문 등록 API 입니다.")
    public ApiResponse<QuestionResponseDto.QuestionDto> addQuestion(@RequestBody QuestionRequestDto.QuestionRegisterDto request, @AuthUser Member member) {
        QuestionResponseDto.QuestionDto response = questionCommandService.addQuestion(member, request);
        return ApiResponse.onSuccess(SuccessStatus.QUESTION_REGISTER_OK, response);
    }

    @GetMapping("/")
    @Operation(summary = "질문 등록 API", description = "질문 등록 API 입니다.")
    public ApiResponse<List<QuestionResponseDto.QuestionDto>> getQuestions(@AuthUser Member member) {
        List<QuestionResponseDto.QuestionDto> response = questionQueryService.getQuestions(member);
        return ApiResponse.onSuccess(SuccessStatus.QUESTION_REGISTER_OK, response);
    }
}
