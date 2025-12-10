package com.example.ohmobackend.web.dto.todoDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class TodoResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TodoDto {
        private Long todoId;
        private boolean status;
        private LocalDate date;
    }
}
