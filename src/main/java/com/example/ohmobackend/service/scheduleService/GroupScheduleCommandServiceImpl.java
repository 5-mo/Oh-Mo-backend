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
import com.example.ohmobackend.service.GroupScheduleEventService;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleRequestDto;
import com.example.ohmobackend.web.dto.routineDto.RoutineResponseDto;
import com.example.ohmobackend.web.dto.todoDto.TodoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.ohmobackend.service.scheduleService.DateCalculator.getDatesFromRepeatWeeks;

@Service
@RequiredArgsConstructor
public class GroupScheduleCommandServiceImpl {

    final private GroupScheduleQueryService groupScheduleQueryService;
    final private RoutineRepository routineRepository;
    final private TodoRepository todoRepository;
    final private MemberGroupRepository memberGroupRepository;
    final private ScheduleRepository scheduleRepository;
    final private GroupScheduleEventService groupScheduleEventService;

    public List<RoutineResponseDto.RoutineDto> addGroupRoutine(GroupScheduleRequestDto.GroupScheduleAddRequestDto request, Member member) {
        Group group = groupScheduleQueryService.getGroup(request.getGroupId());

        // 루틴 추가할 권한 없음(해당 그룹의 멤버가 아님)
        MemberGroup memberGroup = validateMemberGroup(member, group);

        Schedule schedule = ScheduleConverter.groupScheduleToEntity(request, group, ScheduleType.ROUTINE, memberGroup);
        // repeatWeek 저장
        if (request.getRoutineWeek() != null && !request.getRoutineWeek().isEmpty()) {
            schedule.getRepeatWeek().addAll(request.getRoutineWeek());
        }
        schedule.getRepeatWeek().addAll(request.getRoutineWeek());
        scheduleRepository.save(schedule);

        List<LocalDate> dates = getDatesFromRepeatWeeks(LocalDate.now(), request.getDate(), request.getRoutineWeek()); // 반복 요일에 해당하는 날짜 리스트
        List<Routine> routineList = dates.stream()
                .map(date -> RoutineConverter.toEntity(schedule, date))
                .collect(Collectors.toList());
        routineRepository.saveAll(routineList);

        groupScheduleEventService.notifyScheduleChange(group.getId(), request.getDate(), ScheduleEventType.ROUTINE_CREATED);
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
        groupScheduleEventService.notifyScheduleChange(group.getId(), request.getDate(), ScheduleEventType.TODO_CREATED);
        return TodoConverter.toTodoDto(todo);
    }

    private MemberGroup validateMemberGroup(Member member, Group group) {
        return memberGroupRepository.findByGroupAndMember(group, member)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_GROUP_NOT_FOUND));
    }
}
