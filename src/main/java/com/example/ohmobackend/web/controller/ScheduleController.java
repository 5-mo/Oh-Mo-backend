package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.scheduleService.ScheduleCommandService;
import com.example.ohmobackend.service.scheduleService.ScheduleQueryService;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleRequestDto;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/schedule")
@Slf4j
public class ScheduleController {

    final private ScheduleCommandService scheduleCommandService;
    final private ScheduleQueryService scheduleQueryService;

    @PostMapping("/routine")
    @Operation(summary = "루틴 등록 API", description = "루틴 등록 API 입니다.")
    public ApiResponse<Object> addRoutine(@RequestBody ScheduleRequestDto.RoutineRequestDto request, @AuthUser Member member) {
        scheduleCommandService.addRoutine(request, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_ROUTINE_OK, null);
    }

    @PostMapping("/todo")
    @Operation(summary = "투두 등록 API", description = "투두 등록 API 입니다.")
    public ApiResponse<Object> addTodo(@RequestBody ScheduleRequestDto.TodoRequestDto request, @AuthUser Member member) {
        scheduleCommandService.addTodo(request, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_TO_DO_OK, null);
    }

    @PostMapping("/{scheduleId}")
    @Operation(summary = "상태 변경 API", description = "상태 변경 API 입니다.")
    public ApiResponse<Object> updateScheduleStatus(@PathVariable(name = "scheduleId") Long scheduleId, @AuthUser Member member) {
        scheduleCommandService.updateScheduleStatus(scheduleId);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_UPDATE_STATUS_OK, null);
    }

    @GetMapping("/by-date")
    @Operation(summary = "일별 일정 조회 API", description = "일별 일정 조회 API 입니다.")
    public ApiResponse<List<ScheduleResponseDto.ScheduleDto>> getScheduleList(@RequestParam(name = "date")LocalDate date, @RequestParam(name = "type")ScheduleType scheduleType, @AuthUser Member member) {
        List<ScheduleResponseDto.ScheduleDto> scheduleDtoList = scheduleQueryService.getScheduleList(date, member, scheduleType);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_OK, scheduleDtoList);
    }

    @GetMapping("/by-month")
    @Operation(summary = "월별 일정 조회 API", description = "월별 일정 조회 API 입니다.")
    public ApiResponse<List<ScheduleResponseDto.ScheduleByMonthDto>> getScheduleListByMonth(@RequestParam(name = "yearMonth")String yearMonth, @AuthUser Member member) {
        List<ScheduleResponseDto.ScheduleByMonthDto> scheduleDtoList = scheduleQueryService.getScheduleListByMonth(yearMonth, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_OK, scheduleDtoList);
    }
}
