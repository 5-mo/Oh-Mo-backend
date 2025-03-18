package com.example.ohmobackend.service.scheduleService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.MemberCategoryHandler;
import com.example.ohmobackend.apiPayload.exception.handler.ScheduleHandler;
import com.example.ohmobackend.converter.ScheduleConverter;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberCategory;
import com.example.ohmobackend.domain.Schedule;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.repository.MemberCategoryRepository;
import com.example.ohmobackend.repository.ScheduleRepository;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Override
    public void addRoutine(ScheduleRequestDto.RoutineRequestDto requestDto, Member member) {
        MemberCategory memberCategory = memberCategoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_CATEGORY_NOT_FOUND));

        if(!member.equals(memberCategory.getMember())) {
            throw new MemberCategoryHandler(ErrorStatus.INVALID_MEMBER_CATEGORY);
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

        if(requestDto.getAlarm() && requestDto.getTime() == null) {
            throw new ScheduleHandler(ErrorStatus.MISSING_TIME);
        }

        scheduleRepository.save(ScheduleConverter.todoToEntity(requestDto, memberCategory));
    }

    // 반복되는 요일에 해당하는 날짜들 반환
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
