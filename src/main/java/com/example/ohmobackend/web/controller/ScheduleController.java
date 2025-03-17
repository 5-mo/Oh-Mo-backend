package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.scheduleService.ScheduleCommandService;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/schedule")
@Slf4j
public class ScheduleController {

    final private ScheduleCommandService scheduleCommandService;

    @PostMapping("/routine")
    @Operation(summary = "루틴 등록 API",description = "루틴 등록 API 입니다.")
    public ApiResponse<Object> signup(@RequestBody ScheduleRequestDto.RoutineRequestDto request, @AuthUser Member member) {
        scheduleCommandService.addRoutine(request, member);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_SIGNUP_OK, null);
    }
}
