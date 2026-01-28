package com.example.ohmobackend.service.scheduleService;


import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.GroupHandler;
import com.example.ohmobackend.converter.GroupConverter;
import com.example.ohmobackend.converter.GroupScheduleConverter;
import com.example.ohmobackend.domain.*;
import com.example.ohmobackend.repository.*;
import com.example.ohmobackend.web.dto.MemberAssigneeDto.MemberAssigneeResponseDto;
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
        List<Todo> todos = todoRepository.findTodoWithScheduleAndAssignees(group);
        List<GroupScheduleResponseDto.GroupScheduleTodoDto> groupScheduleTodoDtos = getScheduleTodoDtos(todos);

        // 루틴 찾기
        List<Routine> rotiunes = routineRepository.findRoutinesWithScheduleAndAssignees(group, date);
        List<GroupScheduleResponseDto.GroupScheduleRoutineDto> groupScheduleRoutineDtos = getScheduleRoutineDtos(rotiunes);
        return GroupScheduleConverter.toGroupSchedulesDto(groupScheduleTodoDtos, groupScheduleRoutineDtos);
    }

    private List<GroupScheduleResponseDto.GroupScheduleTodoDto> getScheduleTodoDtos(List<Todo> todos) {
        return todos.stream()
                .map(t -> {
                    List<ScheduleAssignee> assignees = t.getScheduleAssigneeList();
                    List<MemberAssigneeResponseDto.MemberAssigneeInfoResponseDto> memberGroupInfos = assignees.stream().map(
                            scheduleAssignee -> GroupConverter.toAssigneeDto(scheduleAssignee, scheduleAssignee.getMemberGroup())).collect(Collectors.toList());
                    GroupScheduleResponseDto.GroupTodoWithAssigneeDto groupTodoWithAssigneeDto = GroupScheduleConverter.toGroupTodoWithAssigneeDto(t, memberGroupInfos);
                    return GroupScheduleConverter.toGroupScheduleTodoDto(t.getSchedule(), groupTodoWithAssigneeDto);
                })
                .collect(Collectors.toList());
    }

    private List<GroupScheduleResponseDto.GroupScheduleRoutineDto> getScheduleRoutineDtos(List<Routine> routines) {
        return routines.stream()
                .map(r -> {
                    List<ScheduleAssignee> assignees = r.getScheduleAssigneeList();
                    List<MemberAssigneeResponseDto.MemberAssigneeInfoResponseDto> memberGroupInfos = assignees.stream().map(
                            scheduleAssignee -> GroupConverter.toAssigneeDto(scheduleAssignee, scheduleAssignee.getMemberGroup())).collect(Collectors.toList());
                    GroupScheduleResponseDto.GroupRoutineWithAssigneeDto groupRoutineWithAssigneeDto = GroupScheduleConverter.toGroupRoutineWithAssigneeDto(r, memberGroupInfos);
                    return GroupScheduleConverter.toGroupScheduleRoutineDto(r.getSchedule(), groupRoutineWithAssigneeDto);
                })
                .collect(Collectors.toList());
    }
}
