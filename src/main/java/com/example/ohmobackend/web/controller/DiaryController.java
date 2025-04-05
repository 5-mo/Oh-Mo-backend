package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.diaryService.DiaryCommandService;
import com.example.ohmobackend.web.dto.diaryDto.DiaryRequestDto;
import com.example.ohmobackend.web.dto.diaryDto.DiaryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/diary")
@Slf4j
public class DiaryController {

    final DiaryCommandService diaryCommandService;

    @PostMapping()
    @Operation(summary = "일기 등록 API", description = "일기 등록 API 입니다.")
    public ApiResponse<DiaryResponseDto.AddDiaryResponseDto> addDiary(@RequestBody DiaryRequestDto.AddDiaryRequestDto request, @AuthUser Member member) {
        DiaryResponseDto.AddDiaryResponseDto responseDto = diaryCommandService.addDiary(member, request);
        return ApiResponse.onSuccess(SuccessStatus.DIARY_REGISTER_OK, responseDto);
    }
}
