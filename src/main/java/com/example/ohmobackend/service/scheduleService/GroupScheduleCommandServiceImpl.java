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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.ohmobackend.service.scheduleService.DateCalculator.getDatesFromNowDate;

@Service
@RequiredArgsConstructor
public class GroupScheduleCommandServiceImpl {

    final private GroupRepository groupRepository;
    final private RoutineRepository routineRepository;
    final private TodoRepository todoRepository;
    final private MemberGroupRepository memberGroupRepository;
    final private ScheduleAssigneeRepository scheduleAssigneeRepository;
    final private ScheduleRepository scheduleRepository;
    final private MemberRepository memberRepository;

    public void addGroupRoutine(GroupScheduleRequestDto.GroupScheduleAddRequestDto requestDto, Member member) {
        Group group = groupRepository.findById(requestDto.getGroupId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.GROUP_NOT_FOUND));

        // 루틴 추가할 권한 없음(해당 그룹의 멤버가 아님)
        validateMemberGroup(member, group);

        Schedule schedule = ScheduleConverter.groupScheduleToEntity(requestDto, group, ScheduleType.ROUTINE);

        // repeatWeek 저장
        if (requestDto.getRoutineWeek() != null && !requestDto.getRoutineWeek().isEmpty()) {
            schedule.getRepeatWeek().addAll(requestDto.getRoutineWeek());
        }

        scheduleRepository.save(schedule);

        List<LocalDate> dates = getDatesFromNowDate(requestDto.getDate(), requestDto.getRoutineWeek()); // 반복 요일에 해당하는 날짜 리스트

        List<Routine> routineList = dates.stream()
                .map(date -> RoutineConverter.toEntity(schedule, date))
                .collect(Collectors.toList());

        routineRepository.saveAll(routineList);
    }

    public void addGroupTodo(GroupScheduleRequestDto.GroupScheduleAddRequestDto requestDto, Member member) {
        Group group = groupRepository.findById(requestDto.getGroupId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.GROUP_NOT_FOUND));

        // 루틴 추가할 권한 없음(해당 그룹의 멤버가 아님)
        validateMemberGroup(member, group);


        Schedule schedule = ScheduleConverter.groupScheduleToEntity(requestDto, group, ScheduleType.TO_DO);
        scheduleRepository.save(schedule);
        todoRepository.save(TodoConverter.toEntity(schedule));
    }

    public void addTodoScheduleAssignee(GroupScheduleRequestDto.TodoScheduleAssigneeRequestDto requestDto, Member member) {
        Todo todo = todoRepository.findById(requestDto.getTodoId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));

        // 그룹의 스케줄이 아닐 경우
        Group group = todo.getSchedule().getGroup();
        validateScheduleHasGroup(group);

        // 그룹의 멤버가 아닐 경우
        validateMemberGroup(member, group);

        List<ScheduleAssignee> assignees = requestDto.getMemberIdList().stream()
                .map(memberId -> {
                    Member targetMember = memberRepository.findById(memberId)
                            .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_NOT_FOUND));

                    validateMemberGroup(targetMember, group);

                    return ScheduleConverter.todoScheduleAssigneeToEntity(targetMember, todo);
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
        validateMemberGroup(member, group);

        List<ScheduleAssignee> assignees = requestDto.getMemberIdList().stream()
                .map(memberId -> {
                    Member targetMember = memberRepository.findById(memberId)
                            .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_NOT_FOUND));

                    validateMemberGroup(targetMember, group);

                    return ScheduleConverter.routineScheduleAssigneeToEntity(targetMember, routine);
                })
                .collect(Collectors.toList());

        scheduleAssigneeRepository.saveAll(assignees);
    }

    private void validateMemberGroup(Member member, Group group) {
        memberGroupRepository.findByGroupAndMember(group, member)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_GROUP_NOT_FOUND));
    }

    private static void validateScheduleHasGroup(Group group) {
        if(group == null) {
            throw new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_GROUP_TYPE);
        }
    }
}
