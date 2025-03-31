package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.answerService.AnswerCommandService;
import com.example.ohmobackend.web.dto.answerDto.AnswerRequestDto;
import com.example.ohmobackend.web.dto.answerDto.AnswerResponseDto;
import com.example.ohmobackend.web.dto.questionDto.QuestionResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/answer")
public class AnswerController {

    final AnswerCommandService answerCommandService;

    @PostMapping("/")
    @Operation(summary = "질문 답 등록 API",description = "질문 답 등록 API 입니다.")
    public ApiResponse<AnswerResponseDto.AnswerDto> addAnswer(@RequestBody AnswerRequestDto.AddAnswerDto request, @AuthUser Member member) {
        AnswerResponseDto.AnswerDto responseDto = answerCommandService.addAnswer(member, request);
        return ApiResponse.onSuccess(SuccessStatus.ANSWER_REGISTER_OK, responseDto);
    }
}
