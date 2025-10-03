package com.example.ohmobackend.service.todoService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.MemberHandler;
import com.example.ohmobackend.apiPayload.exception.handler.ScheduleHandler;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.Todo;
import com.example.ohmobackend.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TodoCommandServiceImpl implements TodoCommandService{

    final TodoRepository todoRepository;

    @Override
    @Transactional
    public void updateTodoStatus(Long todoId, Member member) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));

        if(todo.getSchedule().getMemberCategory().getMember() != member) {
            throw new MemberHandler(ErrorStatus.INVALID_MEMBER);
        }

        todo.updateStatus();
    }
}
