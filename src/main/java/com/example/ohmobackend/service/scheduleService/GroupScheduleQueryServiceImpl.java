package com.example.ohmobackend.service.scheduleService;


import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.GroupHandler;
import com.example.ohmobackend.converter.GroupScheduleConverter;
import com.example.ohmobackend.domain.*;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.repository.*;
import com.example.ohmobackend.service.assigneeService.AssigneeQueryServiceImpl;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupScheduleQueryServiceImpl implements GroupScheduleQueryService {

    final ScheduleRepository scheduleRepository;
    final RoutineRepository routineRepository;
    final TodoRepository todoRepository;
    final GroupRepository groupRepository;
    final MemberGroupRepository memberGroupRepository;
    final ScheduleAssigneeRepository scheduleAssigneeRepository;

    @Override
    public GroupScheduleResponseDto.GroupSchedulesDto getScheduleList(Long groupId, LocalDate date, Member member) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupHandler(ErrorStatus.GROUP_NOT_FOUND));

        // 투두 찾기
        List<Schedule> todoSchedules = scheduleRepository.findSchedulesWithTodoByGroupAndDateAndScheduleType(group, date, ScheduleType.TO_DO);
        List<GroupScheduleResponseDto.GroupScheduleTodoDto> groupScheduleTodoDtos = getScheduleTodoDtos(todoSchedules);

        // 루틴 찾기
        List<Routine> rotiunes = routineRepository.findRoutinesWithScheduleByGroupAndDate(group, date);
        List<GroupScheduleResponseDto.GroupScheduleRoutineDto> groupScheduleRoutineDtos = getScheduleRoutineDtos(rotiunes);
        return GroupScheduleConverter.toGroupSchedulesDto(groupScheduleTodoDtos, groupScheduleRoutineDtos);
    }

    private List<GroupScheduleResponseDto.GroupScheduleTodoDto> getScheduleTodoDtos(List<Schedule> todoSchedules) {
        return todoSchedules.stream()
                .map(s -> {
                    List<ScheduleAssignee> scheduleAssignees = scheduleAssigneeRepository.findAllByTodo(s.getTodo());
                    return GroupScheduleConverter.toGroupScheduleTodoDto(s, scheduleAssignees);
                })
                .collect(Collectors.toList());
    }

    private List<GroupScheduleResponseDto.GroupScheduleRoutineDto> getScheduleRoutineDtos(List<Routine> routines) {
        return routines.stream()
                .map(r -> {
                    List<ScheduleAssignee> scheduleAssignees = scheduleAssigneeRepository.findAllByRoutine(r);
                    return GroupScheduleConverter.toGroupScheduleRoutineDto(r, r.getSchedule(), scheduleAssignees);
                })
                .collect(Collectors.toList());
    }
}
