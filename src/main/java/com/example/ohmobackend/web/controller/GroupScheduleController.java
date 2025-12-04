package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.scheduleService.GroupScheduleCommandServiceImpl;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/group-schedule")
@Slf4j
public class GroupScheduleController {

    final GroupScheduleCommandServiceImpl groupScheduleCommandService;

    @PostMapping("/routine")
    @Operation(summary = "그룹 루틴 등록 API", description = "그룹 루틴 등록 API 입니다.")
    public ApiResponse<Object> addGroupRoutine(@RequestBody GroupScheduleRequestDto.GroupScheduleAddRequestDto request, @AuthUser Member member) {
        groupScheduleCommandService.addGroupRoutine(request, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_ROUTINE_OK, null);
    }

    @PostMapping("/todo")
    @Operation(summary = "그룹 투두 등록 API", description = "그룹 투두 등록 API 입니다.")
    public ApiResponse<Object> addGroupTodo(@RequestBody GroupScheduleRequestDto.GroupScheduleAddRequestDto request, @AuthUser Member member) {
        groupScheduleCommandService.addGroupTodo(request, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_TO_DO_OK, null);
    }

    @PostMapping("/assignee")
    @Operation(summary = "그룹 일정 담당자 등록 API", description = "그룹 일정 담당자 등록 API 입니다.")
    public ApiResponse<Object> addScheduleAssignee(@RequestBody GroupScheduleRequestDto.ScheduleAssigneeDto request, @AuthUser Member member) {
        groupScheduleCommandService.addScheduleAssignee(request, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_ASSIGNEE_OK, null);
    }
}
