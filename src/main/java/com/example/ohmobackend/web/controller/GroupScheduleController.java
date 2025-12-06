package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.assigneeService.AssigneeCommandService;
import com.example.ohmobackend.service.assigneeService.AssigneeQueryService;
import com.example.ohmobackend.service.scheduleService.GroupScheduleCommandServiceImpl;
import com.example.ohmobackend.service.scheduleService.GroupScheduleQueryService;
import com.example.ohmobackend.web.dto.groupDto.GroupResponseDto;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleRequestDto;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/group-schedule")
@Slf4j
public class GroupScheduleController {

    private final GroupScheduleCommandServiceImpl groupScheduleCommandService;
    private final GroupScheduleQueryService groupScheduleQueryService;
    private final AssigneeQueryService assigneeQueryService;
    private final AssigneeCommandService assigneeCommandService;


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

    @PostMapping("/assignee-todo")
    @Operation(summary = "그룹 투두 일정 담당자 등록 API", description = "그룹 투두 일정 담당자 등록 API 입니다.")
    public ApiResponse<Object> addTodoScheduleAssignee(@RequestBody GroupScheduleRequestDto.TodoScheduleAssigneeRequestDto request, @AuthUser Member member) {
        assigneeCommandService.addTodoScheduleAssignee(request, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_ASSIGNEE_OK, null);
    }

    @PostMapping("/assignee-routine")
    @Operation(summary = "그룹 루틴 일정 담당자 등록 API", description = "그룹 루틴 일정 담당자 등록 API 입니다.")
    public ApiResponse<Object> addRoutineScheduleAssignee(@RequestBody GroupScheduleRequestDto.RoutineScheduleAssigneeRequestDto request, @AuthUser Member member) {
        assigneeCommandService.addRoutineScheduleAssignee(request, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_ASSIGNEE_OK, null);
    }

    @GetMapping("/by-date")
    @Operation(summary = "그룹 일별 일정 조회 API", description = "그룹 일별 일정 조회 API 입니다.")
    public ApiResponse<ScheduleResponseDto.ScheduleDto> getScheduleList(@RequestParam(name = "groupId") Long groupId, @RequestParam(name = "date") LocalDate date, @AuthUser Member member) {
        ScheduleResponseDto.ScheduleDto scheduleDtoList = groupScheduleQueryService.getScheduleList(groupId, date, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_OK, scheduleDtoList);
    }

    @GetMapping("/assignee-todo")
    @Operation(summary = "투두 일정 담당자 조회 API")
    public ApiResponse<List<GroupResponseDto.MemberDto>> getTodoAssignees(@RequestParam(name = "todoId") Long todoId, @AuthUser Member member) {
        List<GroupResponseDto.MemberDto> scheduleAssignees = assigneeQueryService.getTodoScheduleAssignee(todoId, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_OK, scheduleAssignees);
    }

    @GetMapping("/assignee-routine")
    @Operation(summary = "루틴 일정 담당자 조회 API")
    public ApiResponse<List<GroupResponseDto.MemberDto>> getRoutineAssignees(@RequestParam(name = "routineId") Long routineId, @AuthUser Member member) {
        List<GroupResponseDto.MemberDto> scheduleAssignees = assigneeQueryService.getRoutineScheduleAssignee(routineId, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_OK, scheduleAssignees);
    }
}
