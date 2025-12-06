package com.example.ohmobackend.service.assigneeService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.GroupHandler;
import com.example.ohmobackend.apiPayload.exception.handler.MemberHandler;
import com.example.ohmobackend.apiPayload.exception.handler.ScheduleHandler;
import com.example.ohmobackend.converter.ScheduleConverter;
import com.example.ohmobackend.domain.*;
import com.example.ohmobackend.repository.MemberGroupRepository;
import com.example.ohmobackend.repository.RoutineRepository;
import com.example.ohmobackend.repository.ScheduleAssigneeRepository;
import com.example.ohmobackend.repository.TodoRepository;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AssigneeCommandServiceImpl implements AssigneeCommandService {

    private final TodoRepository todoRepository;
    private final RoutineRepository routineRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final ScheduleAssigneeRepository scheduleAssigneeRepository;

    public void addTodoScheduleAssignee(GroupScheduleRequestDto.TodoScheduleAssigneeRequestDto requestDto, Member member) {
        Todo todo = todoRepository.findById(requestDto.getTodoId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));

        // 그룹의 스케줄이 아닐 경우
        Group group = todo.getSchedule().getGroup();
        validateScheduleHasGroup(group);

        // 그룹의 멤버가 아닐 경우
        memberGroupRepository.findByGroupAndMember(group, member)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_GROUP_NOT_FOUND));

        List<ScheduleAssignee> assignees = requestDto.getMemberGroupIdList().stream()
                .map(memberGroupId -> {
                    MemberGroup memberGroup = memberGroupRepository.findById(memberGroupId)
                            .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_NOT_FOUND));
                    return ScheduleConverter.todoScheduleAssigneeToEntity(memberGroup, todo);
                })
                .collect(Collectors.toList());

        scheduleAssigneeRepository.saveAll(assignees);
    }

    public void addRoutineScheduleAssignee(GroupScheduleRequestDto.RoutineScheduleAssigneeRequestDto requestDto, Member member) {
        Routine routine = routineRepository.findById(requestDto.getRoutineId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));

        // 그룹의 스케줄이 아닐 경우
        Group group = routine.getSchedule().getGroup();
        validateScheduleHasGroup(group);

        // 그룹의 멤버가 아닐 경우
        memberGroupRepository.findByGroupAndMember(group, member)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_GROUP_NOT_FOUND));

        ;List<ScheduleAssignee> assignees = requestDto.getMemberGroupIdList().stream()
                .map(memberGroupId -> {
                    MemberGroup memberGroup = memberGroupRepository.findById(memberGroupId)
                            .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_NOT_FOUND));
                    return ScheduleConverter.routineScheduleAssigneeToEntity(memberGroup, routine);
                })
                .collect(Collectors.toList());

        scheduleAssigneeRepository.saveAll(assignees);
    }

    public void updateAssigneeStatus(Long assigneeId, Member member) {
        ScheduleAssignee scheduleAssignee = scheduleAssigneeRepository.findById(assigneeId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.ASSIGNEE_NOT_FOUND));

        if (scheduleAssignee.getMemberGroup().getMember() != member) {
            throw new MemberHandler(ErrorStatus.INVALID_MEMBER);
        }

        scheduleAssignee.updateStatus();
        scheduleAssigneeRepository.save(scheduleAssignee);

        //
        AssignableTask task = scheduleAssignee.getTask();

        boolean allCompleted = task.getAssignees().stream()
                .allMatch(ScheduleAssignee::isStatus);

        task.updateStatus(allCompleted);
    }

    private static void validateScheduleHasGroup(Group group) {
        if(group == null) {
            throw new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_GROUP_TYPE);
        }
    }
}
