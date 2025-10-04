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
    public ApiResponse<Object> addRoutine(@RequestBody ScheduleRequestDto.AddRequestDto request, @AuthUser Member member) {
        scheduleCommandService.addRoutine(request, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_ROUTINE_OK, null);
    }

    @PostMapping("/todo")
    @Operation(summary = "투두 등록 API", description = "투두 등록 API 입니다.")
    public ApiResponse<Object> addTodo(@RequestBody ScheduleRequestDto.AddRequestDto request, @AuthUser Member member) {
        scheduleCommandService.addTodo(request, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_TO_DO_OK, null);
    }

    @GetMapping("/by-date")
    @Operation(summary = "일별 일정 조회 API", description = "일별 일정 조회 API 입니다.")
    public ApiResponse<List<ScheduleResponseDto.ScheduleDto>> getScheduleList(@RequestParam(name = "date")LocalDate date, @RequestParam(name = "type")ScheduleType scheduleType, @AuthUser Member member) {
        List<ScheduleResponseDto.ScheduleDto> scheduleDtoList = scheduleQueryService.getScheduleList(date, member, scheduleType);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_OK, scheduleDtoList);
    }

    @GetMapping("/by-month")
    @Operation(summary = "월별 일정 조회 API", description = "월별 일정 조회 API 입니다.")
    public ApiResponse<List<ScheduleResponseDto.ScheduleByMonthDto>> getScheduleListByMonth(@RequestParam(name = "year-month")String yearMonth, @AuthUser Member member) {
        List<ScheduleResponseDto.ScheduleByMonthDto> scheduleDtoList = scheduleQueryService.getScheduleListByMonth(yearMonth, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_OK, scheduleDtoList);
    }

    @PatchMapping ("/update-date")
    @Operation(summary = "투두 날짜 변경 API", description = "투두 날짜 변경 API 입니다.")
    public ApiResponse<ScheduleResponseDto.ScheduleTodoDto> updateScheduleDate(@RequestBody ScheduleRequestDto.UpdateTodoDateRequestDto request, @AuthUser Member member) {
        ScheduleResponseDto.ScheduleTodoDto response = scheduleCommandService.updateScheduleDate(request, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_UPDATE_DATE_OK, response);
    }

//    @GetMapping("/todo/complete")
//    @Operation(summary = "일별 완료한 투두 조회 API", description = "일별 완료한 투두 조회 API 입니다.")
//    public ApiResponse<List<ScheduleResponseDto.ScheduleDto>> getCompleteTodoList(@RequestParam(name = "date")LocalDate date, @AuthUser Member member) {
//        List<ScheduleResponseDto.ScheduleDto> scheduleDtoList = scheduleQueryService.getCompleteTodoList(date, member);
//        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_OK, scheduleDtoList);
//    }

    @GetMapping("")
    @Operation(summary = "검색어로 스케줄 조회 API", description = "검색어로 스케줄 조회 API 입니다.")
    public ApiResponse<List<ScheduleResponseDto.ScheduleDto>> getScheduleList(@RequestParam(name = "query")String keyword, @AuthUser Member member) {
        List<ScheduleResponseDto.ScheduleDto> scheduleDtoList = scheduleQueryService.getScheduleListByKeyword(keyword, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_OK, scheduleDtoList);
    }

    @PatchMapping ("/alarm")
    @Operation(summary = "알람 시간 등록 API", description = "알람 시간 등록 API 입니다.")
    public ApiResponse<Object> updateScheduleAlarmTime(@RequestBody ScheduleRequestDto.UpdateScheduleAlarmTimeDto request, @AuthUser Member member) {
        scheduleCommandService.updateScheduleAlarmTime(request, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_UPDATE_ALARM_TIME_OK, null);
    }

    @GetMapping("routine/status")
    @Operation(summary = "Day Log 주차별 루틴 상태 조회 API", description = "Day Log 주차별 루틴 상태 조회 API 입니다.")
    public ApiResponse<List<ScheduleResponseDto.RoutineStatusByContentDto>> getRoutineStatus(@RequestParam(name = "start-date")LocalDate startDate, @RequestParam(name = "end-date")LocalDate endDate, @AuthUser Member member) {
        List<ScheduleResponseDto.RoutineStatusByContentDto> routineStatusByContentList = scheduleQueryService.getRoutineStatusList(startDate, endDate, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_ROUTINE_STATUS_OK, routineStatusByContentList);
    }

//    @GetMapping("/completion-rate")
//    @Operation(summary = "Day Log 주차별 루틴 상태 조회 API", description = "Day Log 주차별 루틴 상태 조회 API 입니다.")
//    public ApiResponse<List<ScheduleResponseDto.ScheduleCompletionRateByMonthDto>> getScheduleCompletionRateByMonth(@RequestParam(name = "year-month")String yearMonth, @AuthUser Member member) {
//        List<ScheduleResponseDto.ScheduleCompletionRateByMonthDto> routineStatusByContentList = scheduleQueryService.getScheduleCompletionReteByMonth(yearMonth, member);
//        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_COMPLETION_RATE_OK, routineStatusByContentList);
//    }

    @PostMapping("/routine-group")
    @Operation(summary = "그룹 루틴 등록 API", description = "그룹 루틴 등록 API 입니다.")
    public ApiResponse<Object> addGroupRoutine(@RequestBody ScheduleRequestDto.GroupRoutineRequestDto request, @AuthUser Member member) {
        scheduleCommandService.addGroupRoutine(request, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_ROUTINE_OK, null);
    }

    @PostMapping("/todo-group")
    @Operation(summary = "그룹 투두 등록 API", description = "그룹 투두 등록 API 입니다.")
    public ApiResponse<Object> addGroupTodo(@RequestBody ScheduleRequestDto.GroupTodoRequestDto request, @AuthUser Member member) {
        scheduleCommandService.addGroupTodo(request, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_TO_DO_OK, null);
    }

    @PostMapping("/assignee")
    @Operation(summary = "그룹 일정 담당자 등록 API", description = "그룹 일정 담당자 등록 API 입니다.")
    public ApiResponse<Object> addScheduleAssignee(@RequestBody ScheduleRequestDto.ScheduleAssigneeDto request, @AuthUser Member member) {
        scheduleCommandService.addScheduleAssignee(request, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_ASSIGNEE_OK, null);
    }
}
