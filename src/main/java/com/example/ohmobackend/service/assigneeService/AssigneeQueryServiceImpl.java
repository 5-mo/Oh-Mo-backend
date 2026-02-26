package com.example.ohmobackend.service.assigneeService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.ScheduleHandler;
import com.example.ohmobackend.converter.GroupConverter;
import com.example.ohmobackend.converter.GroupScheduleConverter;
import com.example.ohmobackend.domain.*;
import com.example.ohmobackend.repository.MemberGroupRepository;
import com.example.ohmobackend.repository.RoutineRepository;
import com.example.ohmobackend.repository.ScheduleAssigneeRepository;
import com.example.ohmobackend.repository.TodoRepository;
import com.example.ohmobackend.web.dto.MemberAssigneeDto.MemberAssigneeResponseDto;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssigneeQueryServiceImpl implements AssigneeQueryService {

    private final TodoRepository todoRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final ScheduleAssigneeRepository scheduleAssigneeRepository;
    private final RoutineRepository routineRepository;

    @Override
    public GroupScheduleResponseDto.GroupTodoWithAssigneeDto getTodoScheduleAssignee(Long todoId, Member member) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        Group group = todo.getSchedule().getGroup();

        // 그룹의 일정이 아닌 경우
        if (group == null) {
            new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_GROUP_TYPE);
        }

        // 해당 그룹의 멤버가 아님
        memberGroupRepository.findByGroupAndMember(group, member)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_GROUP_NOT_FOUND));

        List<ScheduleAssignee> scheduleAssignees = scheduleAssigneeRepository.findAllByTodo(todo);
        List<MemberAssigneeResponseDto.MemberAssigneeInfoResponseDto> memberGroupInfos = scheduleAssignees.stream().map(
                scheduleAssignee -> GroupConverter.toAssigneeDto(scheduleAssignee, scheduleAssignee.getMemberGroup())).collect(Collectors.toList());
        return GroupScheduleConverter.toGroupTodoWithAssigneeDto(todo, memberGroupInfos);
    }

    @Override
    public GroupScheduleResponseDto.GroupRoutineWithAssigneeDto getRoutineScheduleAssignee(Long routineId, Member member) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        Group group = routine.getSchedule().getGroup();

        // 그룹의 일정이 아닌 경우
        if (group == null) {
            new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_GROUP_TYPE);
        }

        // 해당 그룹의 멤버가 아님
        memberGroupRepository.findByGroupAndMember(group, member)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_GROUP_NOT_FOUND));

        List<ScheduleAssignee> scheduleAssignees = scheduleAssigneeRepository.findAllByRoutine(routine);
        List<MemberAssigneeResponseDto.MemberAssigneeInfoResponseDto> memberGroupInfos = scheduleAssignees.stream().map(
                scheduleAssignee -> GroupConverter.toAssigneeDto(scheduleAssignee, scheduleAssignee.getMemberGroup())).collect(Collectors.toList());
        return GroupScheduleConverter.toGroupRoutineWithAssigneeDto(routine, memberGroupInfos);
    }
}
