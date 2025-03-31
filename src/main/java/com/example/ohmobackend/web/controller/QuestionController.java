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

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/question")
@Slf4j
public class QuestionController {

    final QuestionCommandService questionCommandService;
    final QuestionQueryService questionQueryService;

    @PostMapping()
    @Operation(summary = "질문 등록 API", description = "질문 등록 API 입니다.")
    public ApiResponse<Object> addQuestion(@RequestBody QuestionRequestDto.QuestionRegisterDto request, @AuthUser Member member) {
        questionCommandService.addQuestion(member, request);
        return ApiResponse.onSuccess(SuccessStatus.QUESTION_REGISTER_OK, null);
    }

    @GetMapping()
    @Operation(summary = "질문 조회 API", description = "질문들만 조회하는 API 입니다.")
    public ApiResponse<List<QuestionResponseDto.QuestionDto>> getQuestions(@AuthUser Member member) {
        List<QuestionResponseDto.QuestionDto> response = questionQueryService.getQuestions(member);
        return ApiResponse.onSuccess(SuccessStatus.QUESTION_OK, response);
    }

    @GetMapping("/answer")
    @Operation(summary = "일별 질문 및 답변 조회 API", description = "일별 질문 및 답변 조회 API 입니다.")
    public ApiResponse<List<QuestionResponseDto.QuestionWithAnswerResponseDto>> getAnswers(@AuthUser Member member, @RequestParam(name = "date") LocalDate date) {
        List<QuestionResponseDto.QuestionWithAnswerResponseDto> response = questionQueryService.getQuestionsWithAnswers(member, date);
        return ApiResponse.onSuccess(SuccessStatus.QUESTION_OK, response);
    }
}
