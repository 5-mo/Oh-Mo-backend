package com.example.ohmobackend.service.scheduleService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.ScheduleHandler;
import com.example.ohmobackend.converter.RoutineConverter;
import com.example.ohmobackend.converter.ScheduleConverter;
import com.example.ohmobackend.converter.TodoConverter;
import com.example.ohmobackend.domain.*;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.repository.*;
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

    final private GroupRepository groupRepository;
    final private RoutineRepository routineRepository;
    final private TodoRepository todoRepository;
    final private MemberGroupRepository memberGroupRepository;
    final private ScheduleRepository scheduleRepository;

    public List<RoutineResponseDto.RoutineDto> addGroupRoutine(GroupScheduleRequestDto.GroupScheduleAddRequestDto requestDto, Member member) {
        Group group = groupRepository.findById(requestDto.getGroupId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.GROUP_NOT_FOUND));

        // 루틴 추가할 권한 없음(해당 그룹의 멤버가 아님)
        validateMemberGroup(member, group);

        Schedule schedule = ScheduleConverter.groupScheduleToEntity(requestDto, group, ScheduleType.ROUTINE);

        // repeatWeek 저장
        if (requestDto.getRoutineWeek() != null && !requestDto.getRoutineWeek().isEmpty()) {
            schedule.getRepeatWeek().addAll(requestDto.getRoutineWeek());
        }
        schedule.getRepeatWeek().addAll(requestDto.getRoutineWeek());

        scheduleRepository.save(schedule);

        List<LocalDate> dates = getDatesFromRepeatWeeks(LocalDate.now(), requestDto.getDate(), requestDto.getRoutineWeek()); // 반복 요일에 해당하는 날짜 리스트

        List<Routine> routineList = dates.stream()
                .map(date -> RoutineConverter.toEntity(schedule, date))
                .collect(Collectors.toList());

        routineRepository.saveAll(routineList);

        return routineList.stream()
                .map(RoutineConverter::toRoutineDto)
                .collect(Collectors.toList());
    }

    public TodoResponseDto.TodoDto addGroupTodo(GroupScheduleRequestDto.GroupScheduleAddRequestDto requestDto, Member member) {
        Group group = groupRepository.findById(requestDto.getGroupId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.GROUP_NOT_FOUND));

        // 루틴 추가할 권한 없음(해당 그룹의 멤버가 아님)
        validateMemberGroup(member, group);

        Schedule schedule = ScheduleConverter.groupScheduleToEntity(requestDto, group, ScheduleType.TO_DO);
        scheduleRepository.save(schedule);
        Todo todo = todoRepository.save(TodoConverter.toEntity(schedule));
        return TodoConverter.toTodoDto(todo);
    }

    private void validateMemberGroup(Member member, Group group) {
        memberGroupRepository.findByGroupAndMember(group, member)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_GROUP_NOT_FOUND));
    }
}
