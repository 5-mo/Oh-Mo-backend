package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.Schedule;
import com.example.ohmobackend.domain.Todo;
import com.example.ohmobackend.web.dto.todoDto.TodoResponseDto;

public class TodoConverter {

    public static Todo toEntity(Schedule schedule) {
        return Todo.builder()
                .status(false)
                .schedule(schedule)
                .build();
    }

    public static TodoResponseDto.TodoDto toTodoDto(Todo todo) {
        return TodoResponseDto.TodoDto.builder()
                .todoId(todo.getId())
                .status(todo.isStatus())
                .date(todo.getSchedule().getDate())
                .build();
    }
}
