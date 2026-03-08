package com.example.ohmobackend.service.assigneeService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.code.status.ScheduleEventType;
import com.example.ohmobackend.apiPayload.exception.handler.MemberHandler;
import com.example.ohmobackend.apiPayload.exception.handler.ScheduleHandler;
import com.example.ohmobackend.converter.ScheduleConverter;
import com.example.ohmobackend.domain.*;
import com.example.ohmobackend.repository.MemberGroupRepository;
import com.example.ohmobackend.repository.RoutineRepository;
import com.example.ohmobackend.repository.ScheduleAssigneeRepository;
import com.example.ohmobackend.repository.TodoRepository;
import com.example.ohmobackend.service.ScheduleChangeEvent;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AssigneeCommandServiceImpl implements AssigneeCommandService {

    private final TodoRepository todoRepository;
    private final RoutineRepository routineRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final ScheduleAssigneeRepository scheduleAssigneeRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void addTodoScheduleAssignee(GroupScheduleRequestDto.TodoScheduleAssigneeRequestDto requestDto, Member member) {
        Todo todo = todoRepository.findWithScheduleAndGroupById(requestDto.getTodoId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));

        // 그룹의 스케줄이 아닐
        Schedule schedule = todo.getSchedule();
        Group group = schedule.getGroup();
        validateScheduleHasGroup(group);

        // 그룹의 멤버가 아닐 경우
        memberGroupRepository.findByGroupAndMember(group, member)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_GROUP_NOT_FOUND));

        // 등록하려는 담당자가 그룹의 멤버가 아닌 경우
        MemberGroup memberGroup = memberGroupRepository.findById(requestDto.getMemberGroupId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_NOT_FOUND));
        if (!memberGroup.getGroup().getId().equals(group.getId())) {
            throw new ScheduleHandler(ErrorStatus.MEMBER_GROUP_NOT_FOUND);
        }

        ScheduleAssignee scheduleAssignee = ScheduleConverter.todoScheduleAssigneeToEntity(memberGroup, todo);
        scheduleAssigneeRepository.save(scheduleAssignee);
        eventPublisher.publishEvent(new ScheduleChangeEvent(group.getId(), todo.getDate(), ScheduleEventType.TODO_ASSIGNEE_UPDATED));
    }

    public void addRoutineScheduleAssignee(GroupScheduleRequestDto.RoutineScheduleAssigneeRequestDto requestDto, Member member) {
        Routine routine = routineRepository.findWithScheduleAndGroupById(requestDto.getRoutineId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));

        // 그룹의 스케줄이 아닐 경우
        Schedule schedule = routine.getSchedule();
        Group group = schedule.getGroup();
        validateScheduleHasGroup(group);

        // 그룹의 멤버가 아닐 경우
        memberGroupRepository.findByGroupAndMember(group, member)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_GROUP_NOT_FOUND));

        MemberGroup memberGroup = memberGroupRepository.findById(requestDto.getMemberGroupId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_NOT_FOUND));
        if (!memberGroup.getGroup().getId().equals(group.getId())) {
            throw new ScheduleHandler(ErrorStatus.MEMBER_GROUP_NOT_FOUND);
        }

        ScheduleAssignee scheduleAssignee = ScheduleConverter.routineScheduleAssigneeToEntity(memberGroup, routine);
        scheduleAssigneeRepository.save(scheduleAssignee);
        eventPublisher.publishEvent(new ScheduleChangeEvent(group.getId(), routine.getDate(), ScheduleEventType.ROUTINE_ASSIGNEE_UPDATED));
    }

    public void updateAssigneeStatus(Long assigneeId, Member member) {
        ScheduleAssignee scheduleAssignee = scheduleAssigneeRepository.findById(assigneeId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.ASSIGNEE_NOT_FOUND));

        if (!scheduleAssignee.getMemberGroup().getMember().getId().equals(member.getId())) {
            throw new MemberHandler(ErrorStatus.INVALID_MEMBER);
        }

        // 부모 Task row에 Pessimistic Lock 획득 (race condition 방지)
        // 동일 Todo/Routine에 대한 다른 트랜잭션은 이 락이 풀릴 때까지 대기
        AssignableTask task;
        if (scheduleAssignee.getTodo() != null) {
            task = todoRepository.findByIdWithLock(scheduleAssignee.getTodo().getId())
                    .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        } else {
            task = routineRepository.findByIdWithLock(scheduleAssignee.getRoutine().getId())
                    .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        }

        scheduleAssignee.updateStatus();

        boolean allCompleted = !scheduleAssigneeRepository.existsIncompleteByTask(task.getId());
        task.updateStatus(allCompleted);

        eventPublisher.publishEvent(new ScheduleChangeEvent(scheduleAssignee.getMemberGroup().getGroup().getId(), task.getDate(), ScheduleEventType.STATUS_UPDATED));
    }

    private static void validateScheduleHasGroup(Group group) {
        if(group == null) {
            throw new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_GROUP_TYPE);
        }
    }
}
