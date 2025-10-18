package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.*;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.web.dto.memberCategoryDto.MemberCategoryResponseDto;
import com.example.ohmobackend.web.dto.routineDto.RoutineResponseDto;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleRequestDto;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleResponseDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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
                .category(MemberCategoryConverter.toAddCategoryResponseDto(schedule.getMemberCategory()))
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
                .category(MemberCategoryConverter.toAddCategoryResponseDto(schedule.getMemberCategory()))
                .routineList(routineDtoList)
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

//    static public ScheduleResponseDto.RoutineStatusByContentDto toRoutineStatusByContentDto(List<Schedule> scheduleList, String content) {
//        List<ScheduleResponseDto.ScheduleTodoDto> scheduleDtoList = scheduleList.stream()
//                .map(ScheduleConverter::toScheduleTodoDto).collect(Collectors.toList());
//
//        return ScheduleResponseDto.RoutineStatusByContentDto.builder()
//                .content(content)
//                .scheduleList(scheduleDtoList)
//                .build();
//    }

    static public ScheduleResponseDto.ScheduleCompletionRateByMonthDto toScheduleCompletionRateByMonthDto(LocalDate date, double rate) {
        return ScheduleResponseDto.ScheduleCompletionRateByMonthDto.builder()
                .date(date)
                .rate(rate)
                .build();
    }

    static public Schedule groupRoutineToEntity(
            Group group,
            ScheduleRequestDto.GroupRoutineRequestDto requestDto,
            LocalDate date) {
        return Schedule.builder()
                .date(date)
                .time(requestDto.getTime())
                .alarmTime(requestDto.getAlarmTime())
                .content(requestDto.getContent())
                .scheduleType(ScheduleType.ROUTINE)
                .group(group)
                .build();
    }

    static public Schedule groupTodoToEntity(
            ScheduleRequestDto.GroupTodoRequestDto requestDto,
            Group group) {
        return Schedule.builder()
                .date(requestDto.getDate())
                .time(requestDto.getTime())
                .alarmTime(requestDto.getAlarmTime())
                .content(requestDto.getContent())
                .scheduleType(ScheduleType.TO_DO)
                .group(group)
                .build();
    }

    static public ScheduleAssignee scheduleAssigneeToEntity(
            Member member, Schedule schedule) {
        return ScheduleAssignee.builder()
                .member(member)
                .schedule(schedule)
                .status(false)
                .build();
    }

}
