package com.example.ohmobackend.service.scheduleService;


import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.GroupHandler;
import com.example.ohmobackend.apiPayload.exception.handler.ScheduleHandler;
import com.example.ohmobackend.converter.GroupScheduleConverter;
import com.example.ohmobackend.converter.ScheduleConverter;
import com.example.ohmobackend.domain.*;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.repository.*;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleResponseDto;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupScheduleQueryServiceImpl implements GroupScheduleQueryService {

    final ScheduleRepository scheduleRepository;
    final RoutineRepository routineRepository;
    final GroupRepository groupRepository;
    final MemberGroupRepository memberGroupRepository;
    final ScheduleAssigneeRepository scheduleAssigneeRepository;

    @Override
    public ScheduleResponseDto.ScheduleDto getScheduleList(Long groupId, LocalDate date, Member member) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupHandler(ErrorStatus.GROUP_NOT_FOUND));

        // 투두 찾기
        List<Schedule> todoScheduleList = scheduleRepository.findSchedulesByGroupAndDateAndScheduleType(group, date, ScheduleType.TO_DO);
        List<ScheduleResponseDto.ScheduleTodoDto> scheduleTodoList = todoScheduleList.stream()
                .map(todoSchedule -> ScheduleConverter.toScheduleTodoDto(todoSchedule, todoSchedule.getTodo()))
                .collect(Collectors.toList());

        // 루틴 찾기
        List<Routine> routineList = routineRepository.findRoutinesWithScheduleByGroupAndDate(group, date);

        Map<Schedule, List<Routine>> scheduleToRoutines = routineList.stream()
                .filter(r -> r.getSchedule().getScheduleType() == ScheduleType.ROUTINE)
                .collect(Collectors.groupingBy(Routine::getSchedule));

        List<ScheduleResponseDto.ScheduleWithRoutineListDto> scheduleRoutineList = scheduleToRoutines.entrySet().stream()
                .map(entry -> ScheduleConverter.toScheduleWithRoutineListDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return ScheduleConverter.toScheduleDto(scheduleTodoList, scheduleRoutineList);

    }

    @Override
    public GroupScheduleResponseDto.ScheduleAssigneeDto getScheduleAssignee(Long scheduleId, Member member) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        Group group = schedule.getGroup();

        // 그룹의 일정이 아닌 경우
        if (group == null) {
            new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_GROUP_TYPE);
        }

        // 해당 그룹의 멤버가 아님
        memberGroupRepository.findByGroupAndMember(schedule.getGroup(), member)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_GROUP_NOT_FOUND));

        List<ScheduleAssignee> scheduleAssignees = scheduleAssigneeRepository.findAllBySchedule(schedule);
        List<MemberGroup> memberGroups = scheduleAssignees.stream().map(
                scheduleAssignee -> memberGroupRepository.findByMemberAndGroup(scheduleAssignee.getMember(), group)
        ).collect(Collectors.toList());

        return GroupScheduleConverter.toScheduleAssigneeDto(schedule, memberGroups);
    }
}
