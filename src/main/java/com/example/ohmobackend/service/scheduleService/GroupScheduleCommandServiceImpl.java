package com.example.ohmobackend.service.scheduleService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.code.status.ScheduleEventType;
import com.example.ohmobackend.apiPayload.exception.handler.ScheduleHandler;
import com.example.ohmobackend.converter.RoutineConverter;
import com.example.ohmobackend.converter.ScheduleConverter;
import com.example.ohmobackend.converter.TodoConverter;
import com.example.ohmobackend.domain.*;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.repository.*;
import com.example.ohmobackend.service.ScheduleChangeEvent;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleRequestDto;
import com.example.ohmobackend.web.dto.routineDto.RoutineResponseDto;
import com.example.ohmobackend.web.dto.todoDto.TodoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.ohmobackend.service.scheduleService.DateCalculator.getDatesFromRepeatWeeks;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupScheduleCommandServiceImpl implements GroupScheduleCommandService{

    final private GroupScheduleQueryService groupScheduleQueryService;
    final private RoutineRepository routineRepository;
    final private TodoRepository todoRepository;
    final private MemberGroupRepository memberGroupRepository;
    final private ScheduleRepository scheduleRepository;
    final private ApplicationEventPublisher eventPublisher;

    public List<RoutineResponseDto.RoutineDto> addGroupRoutine(GroupScheduleRequestDto.GroupScheduleAddRequestDto request, Member member) {
        Group group = groupScheduleQueryService.getGroup(request.getGroupId());

        // 루틴 추가할 권한 없음(해당 그룹의 멤버가 아님)
        MemberGroup memberGroup = validateMemberGroup(member, group);

        Schedule schedule = ScheduleConverter.groupScheduleToEntity(request, group, ScheduleType.ROUTINE, memberGroup);
        // repeatWeek 저장
        if (request.getRoutineWeek() != null && !request.getRoutineWeek().isEmpty()) {
            schedule.getRepeatWeek().addAll(request.getRoutineWeek());
        }
        scheduleRepository.save(schedule);

        List<LocalDate> dates = getDatesFromRepeatWeeks(LocalDate.now(), request.getDate(), request.getRoutineWeek()); // 반복 요일에 해당하는 날짜 리스트
        List<Routine> routineList = dates.stream()
                .map(date -> RoutineConverter.toEntity(schedule, date))
                .collect(Collectors.toList());
        routineRepository.saveAll(routineList);

        eventPublisher.publishEvent(new ScheduleChangeEvent(group.getId(), request.getDate(), ScheduleEventType.ROUTINE_CREATED));
        return routineList.stream()
                .map(RoutineConverter::toRoutineDto)
                .collect(Collectors.toList());
    }

    public TodoResponseDto.TodoDto addGroupTodo(GroupScheduleRequestDto.GroupScheduleAddRequestDto request, Member member) {
        Group group = groupScheduleQueryService.getGroup(request.getGroupId());

        // 루틴 추가할 권한 없음(해당 그룹의 멤버가 아님)
        MemberGroup memberGroup = validateMemberGroup(member, group);

        Schedule schedule = ScheduleConverter.groupScheduleToEntity(request, group, ScheduleType.TO_DO, memberGroup);
        scheduleRepository.save(schedule);
        Todo todo = todoRepository.save(TodoConverter.toEntity(schedule));
        eventPublisher.publishEvent(new ScheduleChangeEvent(group.getId(), request.getDate(), ScheduleEventType.TODO_CREATED));
        return TodoConverter.toTodoDto(todo);
    }

    public TodoResponseDto.TodoDto updateGroupTodo(Long todoId, GroupScheduleRequestDto.GroupTodoUpdateRequestDto request, Member member) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        Schedule schedule = todo.getSchedule();
        validateMemberGroup(member, schedule.getGroup());

        if (request.getContent() != null) schedule.updateContent(request.getContent());
        if (request.getDate() != null) schedule.updateDate(request.getDate());
        if (request.getTime() != null) schedule.updateTime(request.getTime());
        if (request.getAlarmTime() != null) schedule.updateAlarmTime(request.getAlarmTime());

        eventPublisher.publishEvent(new ScheduleChangeEvent(schedule.getGroup().getId(), schedule.getDate(), ScheduleEventType.TODO_UPDATED));
        return TodoConverter.toTodoDto(todo);
    }

    public void deleteGroupTodo(Long todoId, Member member) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        Schedule schedule = todo.getSchedule();
        validateMemberGroup(member, schedule.getGroup());

        eventPublisher.publishEvent(new ScheduleChangeEvent(schedule.getGroup().getId(), schedule.getDate(), ScheduleEventType.TODO_DELETED));
        todoRepository.delete(todo);
        scheduleRepository.delete(schedule);
    }

    public List<RoutineResponseDto.RoutineDto> updateGroupRoutine(Long scheduleId, GroupScheduleRequestDto.GroupRoutineUpdateRequestDto request, Member member) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        validateMemberGroup(member, schedule.getGroup());

        boolean routineChanged = false;
        if (request.getContent() != null) schedule.updateContent(request.getContent());
        if (request.getTime() != null) schedule.updateTime(request.getTime());
        if (request.getAlarmTime() != null) schedule.updateAlarmTime(request.getAlarmTime());
        if (request.getDate() != null) { schedule.updateDate(request.getDate()); routineChanged = true; }
        if (request.getRoutineWeek() != null) { schedule.updateRepeatWeek(request.getRoutineWeek()); routineChanged = true; }

        if (routineChanged) {
            updateRoutinesIncrementally(schedule);
        }

        eventPublisher.publishEvent(new ScheduleChangeEvent(schedule.getGroup().getId(), schedule.getDate(), ScheduleEventType.ROUTINE_UPDATED));
        return routineRepository.findAllBySchedule(schedule).stream()
                .map(RoutineConverter::toRoutineDto)
                .collect(Collectors.toList());
    }

    public void deleteGroupRoutine(Long routineId, Member member) {
        Routine routine = routineRepository.findWithScheduleAndGroupById(routineId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        validateMemberGroup(member, routine.getSchedule().getGroup());

        eventPublisher.publishEvent(new ScheduleChangeEvent(routine.getSchedule().getGroup().getId(), routine.getDate(), ScheduleEventType.ROUTINE_DELETED));
        routineRepository.delete(routine);
    }

    private void updateRoutinesIncrementally(Schedule schedule) {
        List<Routine> existingRoutines = routineRepository.findAllBySchedule(schedule);
        Set<LocalDate> existingDates = existingRoutines.stream()
                .map(Routine::getDate)
                .collect(Collectors.toSet());
        Set<LocalDate> newDates = new HashSet<>(getDatesFromRepeatWeeks(LocalDate.now(), schedule.getDate(), schedule.getRepeatWeek()));

        List<Routine> toDelete = existingRoutines.stream()
                .filter(r -> !newDates.contains(r.getDate()))
                .toList();
        routineRepository.deleteAll(toDelete);

        List<Routine> toAdd = newDates.stream()
                .filter(date -> !existingDates.contains(date))
                .map(date -> RoutineConverter.toEntity(schedule, date))
                .toList();
        routineRepository.saveAll(toAdd);
    }

    private MemberGroup validateMemberGroup(Member member, Group group) {
        return memberGroupRepository.findByGroupAndMember(group, member)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_GROUP_NOT_FOUND));
    }
}
