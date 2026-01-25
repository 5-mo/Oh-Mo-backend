package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.Routine;
import com.example.ohmobackend.domain.Schedule;
import com.example.ohmobackend.domain.ScheduleAssignee;
import com.example.ohmobackend.domain.Todo;
import com.example.ohmobackend.web.dto.MemberAssigneeDto.MemberAssigneeResponseDto;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleResponseDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GroupScheduleConverter {

    public static GroupScheduleResponseDto.GroupTodoWithAssigneeDto toGroupTodoWithAssigneeDto(Todo todo, List<ScheduleAssignee> scheduleAssignees) {
        List<MemberAssigneeResponseDto.MemberAssigneeInfoResponseDto> memberGroupInfos = scheduleAssignees.stream().map(
                scheduleAssignee -> GroupConverter.toAssigneeDto(scheduleAssignee)).collect(Collectors.toList());

        return GroupScheduleResponseDto.GroupTodoWithAssigneeDto.builder()
                .todo(TodoConverter.toTodoDto(todo))
                .memberGroupInfos(memberGroupInfos)
                .build();
    }

    public static GroupScheduleResponseDto.GroupRoutineWithAssigneeDto toGroupRoutineWithAssigneeDto(Routine routine, List<ScheduleAssignee> scheduleAssignees) {
        List<MemberAssigneeResponseDto.MemberAssigneeInfoResponseDto> memberGroupInfos = scheduleAssignees.stream().map(
                scheduleAssignee -> GroupConverter.toAssigneeDto(scheduleAssignee)).collect(Collectors.toList());

        return GroupScheduleResponseDto.GroupRoutineWithAssigneeDto.builder()
                .routine(RoutineConverter.toRoutineDto(routine))
                .memberGroupInfos(memberGroupInfos)
                .build();
    }

    public static GroupScheduleResponseDto.GroupScheduleTodoDto toGroupScheduleTodoDto(Schedule schedule, List<ScheduleAssignee> scheduleAssignees) {
        return GroupScheduleResponseDto.GroupScheduleTodoDto.builder()
                .scheduleId(schedule.getId())
                .date(schedule.getDate())
                .time(schedule.getTime())
                .alarmTime(schedule.getAlarmTime())
                .content(schedule.getContent())
                .scheduleType(schedule.getScheduleType())
                .groupTodoWithAssignee(GroupScheduleConverter.toGroupTodoWithAssigneeDto(schedule.getTodo(), scheduleAssignees))
                .build();
    }

    public static GroupScheduleResponseDto.GroupScheduleRoutineDto toGroupScheduleRoutineDto(Routine routine, Schedule schedule, List<ScheduleAssignee> scheduleAssignees) {
        return GroupScheduleResponseDto.GroupScheduleRoutineDto.builder()
                .scheduleId(schedule.getId())
                .date(schedule.getDate())
                .time(schedule.getTime())
                .alarmTime(schedule.getAlarmTime())
                .content(schedule.getContent())
                .scheduleType(schedule.getScheduleType())
                .groupRoutineWithAssignee(GroupScheduleConverter.toGroupRoutineWithAssigneeDto(routine, scheduleAssignees))
                .build();
    }

    public static GroupScheduleResponseDto.GroupSchedulesDto toGroupSchedulesDto(
            List<GroupScheduleResponseDto.GroupScheduleTodoDto> groupScheduleTodoDtos,
            List<GroupScheduleResponseDto.GroupScheduleRoutineDto> groupScheduleRoutineDtos) {
        return GroupScheduleResponseDto.GroupSchedulesDto.builder()
                .todos(groupScheduleTodoDtos)
                .routines(groupScheduleRoutineDtos)
                .build();
    }


}
