package com.example.ohmobackend.service.todoService;

import com.example.ohmobackend.domain.Member;
import org.springframework.transaction.annotation.Transactional;

public interface TodoCommandService {
    @Transactional
    void updateTodoStatus(Long routineId, Member member);
}
