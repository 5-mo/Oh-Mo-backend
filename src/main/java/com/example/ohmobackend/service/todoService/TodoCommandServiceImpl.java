package com.example.ohmobackend.service.todoService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.MemberHandler;
import com.example.ohmobackend.apiPayload.exception.handler.ScheduleHandler;
import com.example.ohmobackend.converter.ScheduleConverter;
import com.example.ohmobackend.converter.TodoConverter;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.Schedule;
import com.example.ohmobackend.domain.Todo;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.repository.ScheduleRepository;
import com.example.ohmobackend.repository.TodoRepository;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleRequestDto;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleResponseDto;
import com.example.ohmobackend.web.dto.todoDto.TodoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TodoCommandServiceImpl implements TodoCommandService{

    final TodoRepository todoRepository;
    final ScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public TodoResponseDto.TodoDto updateTodoStatus(Long todoId, Member member) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));

        if(todo.getSchedule().getMemberCategory().getMember() != member) {
            throw new MemberHandler(ErrorStatus.INVALID_MEMBER);
        }

        todo.updateStatus(!todo.isStatus());
        return TodoConverter.toTodoDto(todo);
    }

    @Override
    @Transactional
    public void deleteTodo(Long todoId, Member member) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));

        if(todo.getSchedule().getMemberCategory().getMember() != member) {
            throw new MemberHandler(ErrorStatus.INVALID_MEMBER);
        }

        Schedule schedule = todo.getSchedule();
        todoRepository.delete(todo);
        todoRepository.flush();
        scheduleRepository.delete(schedule);
    }
}
