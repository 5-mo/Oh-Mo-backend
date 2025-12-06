package com.example.ohmobackend.service.scheduleService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.MemberCategoryHandler;
import com.example.ohmobackend.apiPayload.exception.handler.MemberHandler;
import com.example.ohmobackend.apiPayload.exception.handler.ScheduleHandler;
import com.example.ohmobackend.converter.RoutineConverter;
import com.example.ohmobackend.converter.ScheduleConverter;
import com.example.ohmobackend.converter.TodoConverter;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberCategory;
import com.example.ohmobackend.domain.Routine;
import com.example.ohmobackend.domain.Schedule;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.repository.*;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleRequestDto;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.ohmobackend.service.scheduleService.DateCalculator.getDatesFromNowDate;


@Service
@RequiredArgsConstructor
public class ScheduleCommandServiceImpl implements ScheduleCommandService {

    final private MemberCategoryRepository memberCategoryRepository;
    final private ScheduleRepository scheduleRepository;
    final private MemberRepository memberRepository;
    final private TodoRepository todoRepository;
    final private RoutineRepository routineRepository;

    @Override
    public void addRoutine(ScheduleRequestDto.AddRequestDto requestDto, Member member) {
        MemberCategory memberCategory = memberCategoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_CATEGORY_NOT_FOUND));

        if(!member.equals(memberCategory.getMember())) {
            throw new MemberCategoryHandler(ErrorStatus.INVALID_MEMBER_CATEGORY);
        }

        if(memberCategory.getScheduleType() != ScheduleType.ROUTINE) {
            throw new MemberCategoryHandler(ErrorStatus.MEMBER_CATEGORY_NOT_ROUTINE_TYPE);
        }

        Schedule schedule = ScheduleConverter.toEntity(requestDto, memberCategory);

        // repeatWeek 저장
        if (requestDto.getRoutineWeek() != null && !requestDto.getRoutineWeek().isEmpty()) {
            schedule.getRepeatWeek().addAll(requestDto.getRoutineWeek());
        }

        // DB에 저장
        scheduleRepository.save(schedule);

        // 반복 요일 기반 Routine 생성
        List<LocalDate> dates = getDatesFromNowDate(requestDto.getDate(), requestDto.getRoutineWeek()); // 반복 요일에 해당하는 날짜 리스트

        List<Routine> routineList = dates.stream()
                .map(date -> RoutineConverter.toEntity(schedule, date))
                .collect(Collectors.toList());

        routineRepository.saveAll(routineList);
    }

    @Override
    public void addTodo(ScheduleRequestDto.AddRequestDto requestDto, Member member) {
        MemberCategory memberCategory = memberCategoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_CATEGORY_NOT_FOUND));

        if(!member.equals(memberCategory.getMember())) {
            throw new MemberCategoryHandler(ErrorStatus.INVALID_MEMBER_CATEGORY);
        }

        if(memberCategory.getScheduleType() != ScheduleType.TO_DO) {
            throw new MemberCategoryHandler(ErrorStatus.MEMBER_CATEGORY_NOT_TO_DO_TYPE);
        }

        Schedule schedule = scheduleRepository.save(ScheduleConverter.toEntity(requestDto, memberCategory));
        todoRepository.save(TodoConverter.toEntity(schedule));
    }

    @Override
    @Transactional
    public ScheduleResponseDto.ScheduleTodoDto updateScheduleDate(ScheduleRequestDto.UpdateTodoDateRequestDto requestDto, Member member) {
        Schedule schedule = scheduleRepository.findById(requestDto.getScheduleId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));

        if(schedule.getScheduleType() == ScheduleType.ROUTINE) {
            throw new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_TO_DO_TYPE);
        }

        if(schedule.getMemberCategory().getMember() != member) {
            throw new MemberHandler(ErrorStatus.INVALID_MEMBER);
        }

        schedule.updateDate(requestDto.getDate());

        return ScheduleConverter.toScheduleTodoDto(schedule, schedule.getTodo());
    }

    @Override
    public void updateScheduleAlarmTime(ScheduleRequestDto.UpdateScheduleAlarmTimeDto requestDto, Member member) {
        Schedule schedule = scheduleRepository.findById(requestDto.getScheduleId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));

        if(schedule.getMemberCategory().getMember() != member) {
            throw new MemberHandler(ErrorStatus.INVALID_MEMBER);
        }

        schedule.updateAlarmTime(requestDto.getAlarmTime());
    }
}
