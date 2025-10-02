package com.example.ohmobackend.service.scheduleService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.MemberCategoryHandler;
import com.example.ohmobackend.apiPayload.exception.handler.ScheduleHandler;
import com.example.ohmobackend.converter.ScheduleConverter;
import com.example.ohmobackend.domain.*;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.repository.*;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleRequestDto;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ScheduleCommandServiceImpl implements ScheduleCommandService {

    final private MemberCategoryRepository memberCategoryRepository;
    final private ScheduleRepository scheduleRepository;
    final private GroupRepository groupRepository;
    final private MemberGroupRepository memberGroupRepository;

    @Override
    public void addRoutine(ScheduleRequestDto.RoutineRequestDto requestDto, Member member) {
        MemberCategory memberCategory = memberCategoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_CATEGORY_NOT_FOUND));

        if(!member.equals(memberCategory.getMember())) {
            throw new MemberCategoryHandler(ErrorStatus.INVALID_MEMBER_CATEGORY);
        }

        if(memberCategory.getScheduleType() != ScheduleType.ROUTINE) {
            throw new MemberCategoryHandler(ErrorStatus.MEMBER_CATEGORY_NOT_TO_DO_TYPE);
        }

        // 알람 설정이 true 이지만 시간이 없을 경우
        if(requestDto.getAlarm() && requestDto.getTime() == null) {
            throw new ScheduleHandler(ErrorStatus.MISSING_TIME);
        }

        LocalDate startDate = LocalDate.now();  // 시작 날짜 (오늘)
        LocalDate endDate = requestDto.getEndDate();
        List<LocalDate> dates = getDates(startDate, endDate, requestDto.getRoutineWeek()); // 반복 요일에 해당하는 날짜 리스트

        List<Schedule> schedules = dates.stream()
                .map(date -> ScheduleConverter.routineToEntity(requestDto, memberCategory, date))
                .collect(Collectors.toList());

        scheduleRepository.saveAll(schedules);
    }

    @Override
    public void addTodo(ScheduleRequestDto.TodoRequestDto requestDto, Member member) {
        MemberCategory memberCategory = memberCategoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_CATEGORY_NOT_FOUND));

        if(!member.equals(memberCategory.getMember())) {
            throw new MemberCategoryHandler(ErrorStatus.INVALID_MEMBER_CATEGORY);
        }

        if(memberCategory.getScheduleType() != ScheduleType.TO_DO) {
            throw new MemberCategoryHandler(ErrorStatus.MEMBER_CATEGORY_NOT_TO_DO_TYPE);
        }

        // 알람 설정이 true 이지만 시간이 없을 경우
        if(requestDto.getAlarm() && requestDto.getTime() == null) {
            throw new ScheduleHandler(ErrorStatus.MISSING_TIME);
        }

        scheduleRepository.save(ScheduleConverter.todoToEntity(requestDto, memberCategory));
    }

    @Override
    @Transactional
    public void updateScheduleStatus(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));

        schedule.updateStatus();
    }

    @Override
    @Transactional
    public ScheduleResponseDto.ScheduleDto updateScheduleDate(ScheduleRequestDto.UpdateTodoDateRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(requestDto.getScheduleId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));

        if(schedule.getScheduleType() == ScheduleType.ROUTINE) {
            throw new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_TO_DO_TYPE);
        }
        schedule.updateDate(requestDto.getDate());

        return ScheduleConverter.toScheduleDto(schedule);
    }

    @Override
    public void updateScheduleAlarmTime(ScheduleRequestDto.UpdateScheduleAlarmTimeDto requestDto) {
        Schedule schedule = scheduleRepository.findById(requestDto.getScheduleId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));

        if(!schedule.isAlarm() || schedule.getTime() == null) {
            throw new ScheduleHandler(ErrorStatus.SCHEDULE_INVALID_ALARM_TIME);
        }

        if(schedule.getScheduleType() == ScheduleType.ROUTINE) {
            String scheduleContent = schedule.getContent();
            List<Schedule> routineScheduleList = scheduleRepository.findByContent(scheduleContent);

            routineScheduleList.forEach(routineSchedule ->
                    routineSchedule.updateAlarmTime(requestDto.getTime())
            );
        } else {
            schedule.updateAlarmTime(requestDto.getTime());
        }
    }

    // 반복되는 요일에 해당하는 날짜들 반환
    @Override
    public void addGroupRoutine(ScheduleRequestDto.GroupRoutineRequestDto requestDto, Member member) {
        Group group = groupRepository.findById(requestDto.getGroupId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.GROUP_NOT_FOUND));

        // 루틴 추가할 권한 없음(해당 그룹의 멤버가 아님)
        memberGroupRepository.findByGroupAndMember(group, member)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_GROUP_NOT_FOUND));

        // 알람 설정이 true 이지만 시간이 없을 경우
        if(requestDto.getAlarm() && requestDto.getTime() == null) {
            throw new ScheduleHandler(ErrorStatus.MISSING_TIME);
        }

        LocalDate startDate = LocalDate.now();  // 시작 날짜 (오늘)
        LocalDate endDate = requestDto.getEndDate();
        List<LocalDate> dates = getDates(startDate, endDate, requestDto.getRoutineWeek()); // 반복 요일에 해당하는 날짜 리스트

        List<Schedule> schedules = dates.stream()
                .map(date -> ScheduleConverter.groupRoutineToEntity(group, requestDto, date))
                .collect(Collectors.toList());

        scheduleRepository.saveAll(schedules);
    }

    public static List<LocalDate> getDates(LocalDate startDate, LocalDate endDate, List<DayOfWeek> weeks) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            if (weeks.contains(dayOfWeek)) {
                dates.add(currentDate);
            }
            currentDate = currentDate.plusDays(1); // 하루씩 증가
        }

        return dates;
    }
}
