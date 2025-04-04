package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.dayLogService.DayLogCommandService;
import com.example.ohmobackend.web.dto.dayLogDto.DayLogRequestDto;
import com.example.ohmobackend.web.dto.dayLogDto.DayLogResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/day-log")
@Slf4j
public class DayLogController {

    final DayLogCommandService dayLogCommandService;

    @PostMapping()
    @Operation(summary = "이모지 등록 API", description = "이모지 등록 API 입니다.")
    public ApiResponse<DayLogResponseDto.AddDayLogResponseDto> addEmoji(@RequestBody DayLogRequestDto.AddDayLogRequestDto request, @AuthUser Member member) {
        DayLogResponseDto.AddDayLogResponseDto responseDto = dayLogCommandService.addDayLog(member, request);
        return ApiResponse.onSuccess(SuccessStatus.QUESTION_REGISTER_OK, responseDto);
    }
}
