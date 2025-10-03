package com.example.ohmobackend.web.controller;


import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.todoService.TodoCommandService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/todo")
@Slf4j
public class TodoController {

    final TodoCommandService todoCommandService;

    @PatchMapping("/{todoId}")
    @Operation(summary = "투두 상태 변경 API", description = "상태 변경 API 입니다.")
    public ApiResponse<Object> updateRoutineStatus(@PathVariable(name = "todoId") Long todoId, @AuthUser Member member) {
        todoCommandService.updateTodoStatus(todoId, member);
        return ApiResponse.onSuccess(SuccessStatus.ROUTINE_UPDATE_STATUS_OK, null);
    }

}
