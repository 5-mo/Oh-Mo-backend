package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.Schedule;
import com.example.ohmobackend.domain.Todo;

public class TodoConverter {

    public static Todo toEntity(Schedule schedule) {
        return Todo.builder()
                .status(false)
                .schedule(schedule)
                .build();
    }
}
