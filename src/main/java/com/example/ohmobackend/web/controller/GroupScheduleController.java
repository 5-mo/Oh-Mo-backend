package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.assigneeService.AssigneeCommandService;
import com.example.ohmobackend.service.assigneeService.AssigneeQueryService;
import com.example.ohmobackend.service.scheduleService.GroupScheduleCommandService;
import com.example.ohmobackend.service.scheduleService.GroupScheduleQueryService;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleRequestDto;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleResponseDto;
import com.example.ohmobackend.web.dto.routineDto.RoutineResponseDto;
import com.example.ohmobackend.web.dto.todoDto.TodoResponseDto;
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

    private final GroupScheduleCommandService groupScheduleCommandService;
    private final GroupScheduleQueryService groupScheduleQueryService;
    private final AssigneeQueryService assigneeQueryService;
    private final AssigneeCommandService assigneeCommandService;


    @PostMapping("/routine")
    @Operation(summary = "그룹 루틴 등록 API", description = "그룹 루틴 등록 API 입니다.")
    public ApiResponse<List<RoutineResponseDto.RoutineDto>> addGroupRoutine(@RequestBody GroupScheduleRequestDto.GroupScheduleAddRequestDto request, @AuthUser Member member) {
        List<RoutineResponseDto.RoutineDto> routineDtos = groupScheduleCommandService.addGroupRoutine(request, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_ROUTINE_OK, routineDtos);
    }

    @PostMapping("/todo")
    @Operation(summary = "그룹 투두 등록 API", description = "그룹 투두 등록 API 입니다.")
    public ApiResponse<TodoResponseDto.TodoDto> addGroupTodo(@RequestBody GroupScheduleRequestDto.GroupScheduleAddRequestDto request, @AuthUser Member member) {
        TodoResponseDto.TodoDto todoDto = groupScheduleCommandService.addGroupTodo(request, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_TO_DO_OK, todoDto);
    }

    @PostMapping("/nlp/todo")
    @Operation(summary = "그룹 투두 AI 일정 등록 API",
            description = "그룹 투두 AI 일정 등록 API입니다. 입력 필수 요소: groupId, text")
    public ApiResponse<TodoResponseDto.TodoDto> nlpAddGroupTodo(
            @RequestBody GroupScheduleRequestDto.GroupNlpAddRequestDto request,
            @AuthUser Member member) {
        TodoResponseDto.TodoDto todoDto = groupScheduleCommandService.nlpAddGroupTodo(request, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_TO_DO_OK, todoDto);
    }

    @PostMapping("/assignee-todo")
    @Operation(summary = "그룹 투두 일정 담당자 등록 API", description = "그룹 투두 일정 담당자 등록 API 입니다.")
    public ApiResponse<Object> addTodoScheduleAssignee(@RequestBody GroupScheduleRequestDto.TodoScheduleAssigneeRequestDto request, @AuthUser Member member) {
        assigneeCommandService.addTodoScheduleAssignee(request, member);
        return ApiResponse.onSuccess(SuccessStatus.ASSIGNEE_ADD_OK, null);
    }

    @PostMapping("/assignee-routine")
    @Operation(summary = "그룹 루틴 일정 담당자 등록 API", description = "그룹 루틴 일정 담당자 등록 API 입니다.")
    public ApiResponse<Object> addRoutineScheduleAssignee(@RequestBody GroupScheduleRequestDto.RoutineScheduleAssigneeRequestDto request, @AuthUser Member member) {
        assigneeCommandService.addRoutineScheduleAssignee(request, member);
        return ApiResponse.onSuccess(SuccessStatus.ASSIGNEE_ADD_OK, null);
    }

    @GetMapping("/by-date")
    @Operation(summary = "그룹 일별 일정 조회 API", description = "그룹 일별 일정 조회 API 입니다.")
    public ApiResponse<GroupScheduleResponseDto.GroupSchedulesDto> getScheduleList(@RequestParam(name = "groupId") Long groupId, @RequestParam(name = "date") LocalDate date, @AuthUser Member member) {
        GroupScheduleResponseDto.GroupSchedulesDto scheduleDtoList = groupScheduleQueryService.getScheduleList(groupId, date, member);
        return ApiResponse.onSuccess(SuccessStatus.SCHEDULE_OK, scheduleDtoList);
    }

    @GetMapping("/assignee-todo")
    @Operation(summary = "투두 일정 담당자 조회(완료 상태 조회) API")
    public ApiResponse<GroupScheduleResponseDto.GroupTodoWithAssigneeDto> getTodoAssignees(@RequestParam(name = "todoId") Long todoId, @AuthUser Member member) {
        GroupScheduleResponseDto.GroupTodoWithAssigneeDto todoScheduleAssignee = assigneeQueryService.getTodoScheduleAssignee(todoId, member);
        return ApiResponse.onSuccess(SuccessStatus.ASSIGNEE_OK, todoScheduleAssignee);
    }

    @GetMapping("/assignee-routine")
    @Operation(summary = "루틴 일정 담당자 조회(완료 상태 조회) API")
    public ApiResponse<GroupScheduleResponseDto.GroupRoutineWithAssigneeDto> getRoutineAssignees(@RequestParam(name = "routineId") Long routineId, @AuthUser Member member) {
        GroupScheduleResponseDto.GroupRoutineWithAssigneeDto routineScheduleAssignee = assigneeQueryService.getRoutineScheduleAssignee(routineId, member);
        return ApiResponse.onSuccess(SuccessStatus.ASSIGNEE_OK, routineScheduleAssignee);
    }

    @PostMapping("/assignee/status")
    @Operation(summary = "담당자 일정 상태 변경 API",
            description = "루틴, 투두 관계 없이 담당자 아이디만 필요, 일전 관련된 모든 담당자가 완료하면 일정의 완료 상태는 자동으로 업데이트")
    public ApiResponse<Object> updateAssigneeStatus(@RequestParam(name = "assigneeId") Long assigneeId, @AuthUser Member member) {
        assigneeCommandService.updateAssigneeStatus(assigneeId, member);
        return ApiResponse.onSuccess(SuccessStatus.ASSIGNEE_STATUS_UPDATE_OK, null);
    }

    @PatchMapping("/todo/{todoId}")
    @Operation(summary = "그룹 투두 수정 API", description = "그룹 투두 수정 API 입니다.")
    public ApiResponse<TodoResponseDto.TodoDto> updateGroupTodo(@PathVariable(name = "todoId") Long todoId, @RequestBody GroupScheduleRequestDto.GroupTodoUpdateRequestDto request, @AuthUser Member member) {
        TodoResponseDto.TodoDto todoDto = groupScheduleCommandService.updateGroupTodo(todoId, request, member);
        return ApiResponse.onSuccess(SuccessStatus.TODO_UPDATE_OK, todoDto);
    }

    @DeleteMapping("/todo/{todoId}")
    @Operation(summary = "그룹 투두 삭제 API", description = "그룹 투두 삭제 API 입니다.")
    public ApiResponse<Object> deleteGroupTodo(@PathVariable(name = "todoId") Long todoId, @AuthUser Member member) {
        groupScheduleCommandService.deleteGroupTodo(todoId, member);
        return ApiResponse.onSuccess(SuccessStatus.TODO_DELETE_OK, null);
    }

    @PatchMapping("/routine/{scheduleId}")
    @Operation(summary = "그룹 루틴 수정 API", description = "그룹 루틴 수정 API 입니다.")
    public ApiResponse<List<RoutineResponseDto.RoutineDto>> updateGroupRoutine(@PathVariable(name = "scheduleId") Long scheduleId, @RequestBody GroupScheduleRequestDto.GroupRoutineUpdateRequestDto request, @AuthUser Member member) {
        List<RoutineResponseDto.RoutineDto> routineDtos = groupScheduleCommandService.updateGroupRoutine(scheduleId, request, member);
        return ApiResponse.onSuccess(SuccessStatus.ROUTINE_UPDATE_OK, routineDtos);
    }

    @DeleteMapping("/routine/{routineId}")
    @Operation(summary = "그룹 루틴 삭제 API", description = "그룹 루틴 삭제 API 입니다.")
    public ApiResponse<Object> deleteGroupRoutine(@PathVariable(name = "routineId") Long routineId, @AuthUser Member member) {
        groupScheduleCommandService.deleteGroupRoutine(routineId, member);
        return ApiResponse.onSuccess(SuccessStatus.ROUTINE_DELETE_OK, null);
    }
}
