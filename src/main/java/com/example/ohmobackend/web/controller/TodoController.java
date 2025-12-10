package com.example.ohmobackend.web.controller;


import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.todoService.TodoCommandService;
import com.example.ohmobackend.web.dto.routineDto.RoutineResponseDto;
import com.example.ohmobackend.web.dto.todoDto.TodoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/todo")
@Slf4j
public class TodoController {

    final TodoCommandService todoCommandService;

    @PatchMapping("/{todoId}")
    @Operation(summary = "투두 상태 변경 API", description = "상태 변경 API 입니다.")
    public ApiResponse<TodoResponseDto.TodoDto> updateRoutineStatus(@PathVariable(name = "todoId") Long todoId, @AuthUser Member member) {
        TodoResponseDto.TodoDto todoDto = todoCommandService.updateTodoStatus(todoId, member);
        return ApiResponse.onSuccess(SuccessStatus.TODO_UPDATE_STATUS_OK, todoDto);
    }

    @DeleteMapping("/{todoId}")
    @Operation(summary = "투두 삭제 API", description = "투두 삭제 API 입니다.")
    public ApiResponse<Object> deleteTodo(@PathVariable(name = "todoId") Long todoId, @AuthUser Member member) {
        todoCommandService.deleteTodo(todoId, member);
        return ApiResponse.onSuccess(SuccessStatus.TODO_DELETE_OK, null);
    }

}
