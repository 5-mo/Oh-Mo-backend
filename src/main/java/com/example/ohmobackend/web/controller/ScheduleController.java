package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.scheduleService.ScheduleCommandService;
import com.example.ohmobackend.service.scheduleService.ScheduleQueryService;
import com.example.ohmobackend.web.dto.routineDto.RoutineResponseDto;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleRequestDto;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleResponseDto;
import com.example.ohmobackend.web.dto.todoDto.TodoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class ScheduleController {

    final private ScheduleCommandService scheduleCommandService;
    final private ScheduleQueryService scheduleQueryService;

    @PostMapping("/routine")
    @Operation(summary = "루틴 등록 API",
            description = "루틴 등록 API 입니다." +
                    "입력 필수 요소: date(끝나는 날짜), 반복 요일, 내용")
    public ApiResponse<List<RoutineResponseDto.RoutineDto>> addRoutine(@Valid @RequestBody ScheduleRequestDto.AddRequestDto request, @AuthUser Member member) {
        List<RoutineResponseDto.RoutineDto> routineDtos = scheduleCommandService.addRoutine(request, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_ROUTINE_OK, routineDtos);
    }

    @PatchMapping("/{scheduleId}/routine")
    @Operation(summary = "루틴 수정 API",
            description = "루틴 수정 API 입니다.")
    public ApiResponse<List<RoutineResponseDto.RoutineDto>> updateRoutine(
            @Valid @RequestBody ScheduleRequestDto.AddRequestDto request,
            @PathVariable(name = "scheduleId") Long scheduleId,
            @AuthUser Member member) {
        List<RoutineResponseDto.RoutineDto> routineDtos = scheduleCommandService.updateRoutine(scheduleId, request, member);
        return ApiResponse.onSuccess(SuccessStatus.ROUTINE_UPDATE_OK, routineDtos);
    }

    @PostMapping("/todo")
    @Operation(summary = "투두 등록 API",
            description = "투두 등록 API 입니다." +
                    "입력 필수 요소: date(투두 날짜), 내용")
    public ApiResponse<TodoResponseDto.TodoDto> addTodo(@Valid @RequestBody ScheduleRequestDto.AddRequestDto request, @AuthUser Member member) {
        TodoResponseDto.TodoDto todoDto = scheduleCommandService.addTodo(request, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_TO_DO_OK, todoDto);
    }

    @PatchMapping("/{scheduleId}/todo")
    @Operation(summary = "투두 수정 API",
            description = "투두 수정 API 입니다." )
    public ApiResponse<TodoResponseDto.TodoDto> updateTodo(
            @Valid @RequestBody ScheduleRequestDto.AddRequestDto request,
            @PathVariable(name = "scheduleId") Long scheduleId,
            @AuthUser Member member) {
        TodoResponseDto.TodoDto todoDto  = scheduleCommandService.updateTodo(scheduleId, request, member);
        return ApiResponse.onSuccess(SuccessStatus.TODO_UPDATE_OK, todoDto);
    }

    @PostMapping("/nlp/todo")
    @Operation(summary = "투두 AI 일정 등록 API",
            description = "투두 AI 일정 등록 API입니다. " +
                    "입력 필수 요소: text")
    public ApiResponse<TodoResponseDto.TodoDto> nlpAddTodo(
            @Valid @RequestBody ScheduleRequestDto.NlpAddRequestDto request,
            @AuthUser Member member) {
        TodoResponseDto.TodoDto todoDto = scheduleCommandService.nlpAddTodo(request, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_TO_DO_OK, todoDto);
    }

    @PatchMapping ("/alarm")
    @Operation(summary = "알람 시간 등록 API", description = "알람 시간 등록 API 입니다.")
    public ApiResponse<Object> updateScheduleAlarmTime(@RequestBody ScheduleRequestDto.UpdateScheduleAlarmTimeDto request, @AuthUser Member member) {
        scheduleCommandService.updateScheduleAlarmTime(request, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_UPDATE_ALARM_TIME_OK, null);
    }

    @PatchMapping ("/update-date")
    @Operation(summary = "투두 날짜 변경 API", description = "투두 날짜 변경 API 입니다.")
    public ApiResponse<ScheduleResponseDto.ScheduleTodoDto> updateScheduleDate(@RequestBody ScheduleRequestDto.UpdateTodoDateRequestDto request, @AuthUser Member member) {
        ScheduleResponseDto.ScheduleTodoDto response = scheduleCommandService.updateScheduleDate(request, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_UPDATE_DATE_OK, response);
    }

    // 일정 조회 관련
    @GetMapping("/by-date")
    @Operation(summary = "일별 일정 조회 API", description = "일별 일정 조회 API 입니다.")
    public ApiResponse<ScheduleResponseDto.ScheduleByDateDto> getScheduleList(@RequestParam(name = "date")LocalDate date, @AuthUser Member member) {
        ScheduleResponseDto.ScheduleByDateDto scheduleDtoList = scheduleQueryService.getScheduleList(date, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_OK, scheduleDtoList);
    }

    @GetMapping("/by-month")
    @Operation(summary = "월별 일정 조회 API", description = "월별 일정 조회 API 입니다.")
    public ApiResponse<List<ScheduleResponseDto.ScheduleByMonthDto>> getScheduleListByMonth(@RequestParam(name = "year-month")String yearMonth, @AuthUser Member member) {
        List<ScheduleResponseDto.ScheduleByMonthDto> scheduleDtoList = scheduleQueryService.getScheduleListByMonth(yearMonth, member);

        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_OK, scheduleDtoList);

    }

    @GetMapping("/todo/complete")
    @Operation(summary = "일별 완료한 투두 조회 API", description = "일별 완료한 투두 조회 API 입니다.")
    public ApiResponse<List<ScheduleResponseDto.ScheduleTodoDto>> getCompleteTodoList(@RequestParam(name = "date")LocalDate date, @AuthUser Member member) {
        List<ScheduleResponseDto.ScheduleTodoDto> scheduleTodoDtoList = scheduleQueryService.getCompleteTodoList(date, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_OK, scheduleTodoDtoList);
    }

    @GetMapping("")
    @Operation(summary = "검색어로 스케줄 조회 API", description = "검색어로 스케줄 조회 API 입니다.")
    public ApiResponse<ScheduleResponseDto.ScheduleByKeyWordDto> getScheduleList(@RequestParam(name = "query")String keyword, @AuthUser Member member) {
        ScheduleResponseDto.ScheduleByKeyWordDto scheduleDtoList = scheduleQueryService.getScheduleListByKeyword(keyword, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_OK, scheduleDtoList);
    }

    @GetMapping("routine/status")
    @Operation(summary = "Day Log 주차별 루틴 상태 조회 API", description = "Day Log 주차별 루틴 상태 조회 API 입니다.")
    public ApiResponse<List<ScheduleResponseDto.ScheduleWithRoutineListDto>> getRoutineStatus(@RequestParam(name = "start-date")LocalDate startDate, @RequestParam(name = "end-date")LocalDate endDate, @AuthUser Member member) {
        List<ScheduleResponseDto.ScheduleWithRoutineListDto> routineStatusByContentList = scheduleQueryService.getRoutineStatusList(startDate, endDate, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_ROUTINE_STATUS_OK, routineStatusByContentList);
    }
}
