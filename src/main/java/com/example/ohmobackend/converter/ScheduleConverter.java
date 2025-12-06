package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.*;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleRequestDto;
import com.example.ohmobackend.web.dto.memberCategoryDto.MemberCategoryResponseDto;
import com.example.ohmobackend.web.dto.routineDto.RoutineResponseDto;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleRequestDto;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleResponseDto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ScheduleConverter {

    static public Schedule toEntity(
            ScheduleRequestDto.AddRequestDto requestDto,
            MemberCategory memberCategory) {
        return Schedule.builder()
                .date(requestDto.getDate())
                .time(requestDto.getTime())
                .alarmTime(requestDto.getAlarmTime())
                .content(requestDto.getContent())
                .scheduleType(memberCategory.getScheduleType())
                .memberCategory(memberCategory)
                .repeatWeek(new HashSet<>())
                .build();

    }

    static public Schedule groupScheduleToEntity(
            GroupScheduleRequestDto.GroupScheduleAddRequestDto requestDto,
            Group group, ScheduleType scheduleType) {
        return Schedule.builder()
                .date(requestDto.getDate())
                .time(requestDto.getTime())
                .alarmTime(requestDto.getAlarmTime())
                .content(requestDto.getContent())
                .scheduleType(scheduleType)
                .group(group)
                .repeatWeek(new HashSet<>())
                .build();

    }

    static public ScheduleResponseDto.ScheduleDto toScheduleDto(List<ScheduleResponseDto.ScheduleTodoDto> todoList,
                                                                List<ScheduleResponseDto.ScheduleWithRoutineListDto> routineList) {
        return ScheduleResponseDto.ScheduleDto.builder()
                .todoList(todoList)
                .routineList(routineList)
                .build();
    }

    static public ScheduleResponseDto.ScheduleTodoDto toScheduleTodoDto(
            Schedule schedule, Todo todo) {
        return ScheduleResponseDto.ScheduleTodoDto.builder()
                .scheduleId(schedule.getId())
                .date(schedule.getDate())
                .time(schedule.getTime() != null ? schedule.getTime() : null)
                .alarmTime(schedule.getAlarmTime())
                .content(schedule.getContent())
                .scheduleType(schedule.getScheduleType())
                .category(Optional.ofNullable(schedule.getMemberCategory())
                        .map(MemberCategoryConverter::toAddCategoryResponseDto)
                        .orElse(null))
                .todo(TodoConverter.toTodoDto(todo))
                .build();
    }

    static public ScheduleResponseDto.ScheduleRoutineDto toScheduleRoutineDto(
            Schedule schedule, Routine routine) {

        return ScheduleResponseDto.ScheduleRoutineDto.builder()
                .scheduleId(schedule.getId())
                .date(schedule.getDate())
                .time(schedule.getTime() != null ? schedule.getTime() : null)
                .alarmTime(schedule.getAlarmTime())
                .content(schedule.getContent())
                .scheduleType(schedule.getScheduleType())
                .category(MemberCategoryConverter.toAddCategoryResponseDto(schedule.getMemberCategory()))
                .routine(RoutineConverter.toRoutineDto(routine))
                .build();
    }

    static public ScheduleResponseDto.ScheduleWithRoutineListDto toScheduleWithRoutineListDto(
            Schedule schedule, List<Routine> routineList) {

        List<RoutineResponseDto.routineDto> routineDtoList = routineList.stream()
                .map(routine -> RoutineConverter.toRoutineDto(routine))
                .toList();

        return ScheduleResponseDto.ScheduleWithRoutineListDto.builder()
                .scheduleId(schedule.getId())
                .date(schedule.getDate())
                .time(schedule.getTime() != null ? schedule.getTime() : null)
                .alarmTime(schedule.getAlarmTime())
                .content(schedule.getContent())
                .scheduleType(schedule.getScheduleType())
                .category(Optional.ofNullable(schedule.getMemberCategory())
                        .map(MemberCategoryConverter::toAddCategoryResponseDto)
                        .orElse(null))
                .routineByDateList(routineDtoList)
                .build();
    }

    static public ScheduleResponseDto.ScheduleByMonthDto toScheduleByMonthDto(List<Schedule> scheduleList, LocalDate date) {

        List<MemberCategoryResponseDto.CategoryResponseDto> categoryList = scheduleList.stream()
                .map(schedule -> MemberCategoryConverter.toAddCategoryResponseDto(schedule.getMemberCategory()))
                .collect(Collectors.toMap(
                        category -> category.getId(),  // 중복을 제거할 기준 필드
                        category -> category,
                        (existing, replacement) -> existing))
                .values()
                .stream()
                .collect(Collectors.toList());

        return ScheduleResponseDto.ScheduleByMonthDto.builder()
                .date(date)
                .categoryList(categoryList)
                .build();
    }

    static public ScheduleResponseDto.ScheduleCompletionRateByMonthDto toScheduleCompletionRateByMonthDto(LocalDate date, double rate) {
        return ScheduleResponseDto.ScheduleCompletionRateByMonthDto.builder()
                .date(date)
                .rate(rate)
                .build();
    }

    static public ScheduleAssignee todoScheduleAssigneeToEntity(
            MemberGroup memberGroup, Todo todo) {
        return ScheduleAssignee.builder()
                .memberGroup(memberGroup)
                .todo(todo)
                .status(false)
                .build();
    }

    static public ScheduleAssignee routineScheduleAssigneeToEntity(
            MemberGroup memberGroup, Routine routine) {
        return ScheduleAssignee.builder()
                .memberGroup(memberGroup)
                .routine(routine)
                .status(false)
                .build();
    }

}
