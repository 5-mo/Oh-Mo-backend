package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.Routine;
import com.example.ohmobackend.domain.Schedule;
import com.example.ohmobackend.domain.Todo;
import com.example.ohmobackend.web.dto.MemberAssigneeDto.MemberAssigneeResponseDto;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleResponseDto;

import java.util.List;

public class GroupScheduleConverter {

    public static GroupScheduleResponseDto.GroupTodoWithAssigneeDto toGroupTodoWithAssigneeDto(Todo todo,
                                                                                               List<MemberAssigneeResponseDto.MemberAssigneeInfoResponseDto> memberGroupInfos) {
        return GroupScheduleResponseDto.GroupTodoWithAssigneeDto.builder()
                .todo(TodoConverter.toTodoDto(todo))
                .memberGroupInfos(memberGroupInfos)
                .build();
    }

    public static GroupScheduleResponseDto.GroupRoutineWithAssigneeDto toGroupRoutineWithAssigneeDto(Routine routine,
                                                                                                     List<MemberAssigneeResponseDto.MemberAssigneeInfoResponseDto> memberGroupInfos) {

        return GroupScheduleResponseDto.GroupRoutineWithAssigneeDto.builder()
                .routine(RoutineConverter.toRoutineDto(routine))
                .memberGroupInfos(memberGroupInfos)
                .build();
    }

    public static GroupScheduleResponseDto.GroupScheduleTodoDto toGroupScheduleTodoDto(Schedule schedule,
                                                                                       GroupScheduleResponseDto.GroupTodoWithAssigneeDto groupTodoWithAssigneeDto) {
        return GroupScheduleResponseDto.GroupScheduleTodoDto.builder()
                .scheduleId(schedule.getId())
                .date(schedule.getDate())
                .time(schedule.getTime())
                .alarmTime(schedule.getAlarmTime())
                .content(schedule.getContent())
                .scheduleType(schedule.getScheduleType())
                .groupTodoWithAssignee(groupTodoWithAssigneeDto)
                .build();
    }

    public static GroupScheduleResponseDto.GroupScheduleRoutineDto toGroupScheduleRoutineDto(Schedule schedule,
                                                                                             GroupScheduleResponseDto.GroupRoutineWithAssigneeDto groupRoutineWithAssignee) {
        return GroupScheduleResponseDto.GroupScheduleRoutineDto.builder()
                .scheduleId(schedule.getId())
                .date(schedule.getDate())
                .time(schedule.getTime())
                .alarmTime(schedule.getAlarmTime())
                .content(schedule.getContent())
                .scheduleType(schedule.getScheduleType())
                .groupRoutineWithAssignee(groupRoutineWithAssignee)
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
