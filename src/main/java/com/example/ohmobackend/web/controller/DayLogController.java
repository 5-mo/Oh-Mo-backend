package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.dayLogService.DayLogCommandService;
import com.example.ohmobackend.service.dayLogService.DayLogQueryService;
import com.example.ohmobackend.web.dto.dayLogDto.DayLogRequestDto;
import com.example.ohmobackend.web.dto.dayLogDto.DayLogResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/day-log")
@Slf4j
public class DayLogController {

    private final DayLogCommandService dayLogCommandService;
    private final DayLogQueryService dayLogQueryService;

    @PostMapping("/emoji")
    @Operation(summary = "이모지 등록 API", description = "이모지 등록 API 입니다.")
    public ApiResponse<DayLogResponseDto.AddDayLogResponseDto> addEmoji(@RequestBody DayLogRequestDto.AddDayLogRequestDto request, @AuthUser Member member) {
        DayLogResponseDto.AddDayLogResponseDto responseDto = dayLogCommandService.addDayLog(member, request);
        return ApiResponse.onSuccess(SuccessStatus.EMOJI_REGISTER_OK, responseDto);
    }

    @GetMapping("/emoji")
    @Operation(summary = "이모지 조회 API", description = "이모지 조회 API 입니다.")
    public ApiResponse<DayLogResponseDto.AddDayLogResponseDto> getEmoji(@RequestParam(name = "date") LocalDate date, @AuthUser Member member) {
        DayLogResponseDto.AddDayLogResponseDto responseDto = dayLogQueryService.getDayLogEmoji(member, date);
        return ApiResponse.onSuccess(SuccessStatus.EMOJI_OK, responseDto);
    }
}
