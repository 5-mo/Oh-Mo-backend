package com.example.ohmobackend.service.todoService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.routineDto.RoutineResponseDto;
import com.example.ohmobackend.web.dto.todoDto.TodoResponseDto;
import org.springframework.transaction.annotation.Transactional;

public interface TodoCommandService {
    @Transactional
    public TodoResponseDto.TodoDto updateTodoStatus(Long routineId, Member member);

    @Transactional
    public void deleteTodo(Long todoId, Member member);
}
