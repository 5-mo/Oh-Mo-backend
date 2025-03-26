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
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleQueryServiceImpl implements ScheduleQueryService {

    final MemberCategoryRepository memberCategoryRepository;
    final ScheduleRepository scheduleRepository;

    @Override
    public List<ScheduleResponseDto.ScheduleDto> getScheduleList(LocalDate date, Member member, ScheduleType scheduleType) {
        List<MemberCategory> memberCategoryList = memberCategoryRepository.findByMemberAndScheduleType(member, scheduleType);

        if (memberCategoryList.isEmpty()) {
            throw new MemberCategoryHandler(ErrorStatus.MEMBER_CATEGORY_NOT_FOUND);
        }

        List<Schedule> scheduleList = memberCategoryList.stream()
                .map(memberCategory -> scheduleRepository.findByMemberCategoryAndDate(memberCategory, date))
                .flatMap(List::stream)  // List<Schedule>을 평탄화하여 하나의 스트림으로 변환
                .collect(Collectors.toList());

        if (scheduleList.isEmpty()) {
            throw new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_EXIST);
        }

        return scheduleList.stream()
                .map(schedule -> ScheduleConverter.toScheduleDto(schedule))
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleResponseDto.ScheduleDto> getCompleteTodoList(LocalDate date, Member member) {
        List<MemberCategory> memberCategoryList = memberCategoryRepository.findByMemberAndScheduleType(member, ScheduleType.TO_DO);

        if (memberCategoryList.isEmpty()) {
            throw new MemberCategoryHandler(ErrorStatus.MEMBER_CATEGORY_NOT_FOUND);
        }

        List<Schedule> scheduleList = memberCategoryList.stream()
                .map(memberCategory -> scheduleRepository.findByMemberCategoryAndDateAndStatusIsTrue(memberCategory, date))
                .flatMap(List::stream)  // List<Schedule>을 평탄화하여 하나의 스트림으로 변환
                .collect(Collectors.toList());

        if (scheduleList.isEmpty()) {
            throw new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_EXIST);
        }

        return scheduleList.stream()
                .map(schedule -> ScheduleConverter.toScheduleDto(schedule))
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleResponseDto.ScheduleByMonthDto> getScheduleListByMonth(String yearMonth, Member member) {
        LocalDate firstDayOfMonth = YearMonth.parse(yearMonth).atDay(1);
        LocalDate lastDayOfMonth = YearMonth.parse(yearMonth).atEndOfMonth();

        List<MemberCategory> memberCategoryList = memberCategoryRepository.findByMember(member);

        if (memberCategoryList.isEmpty()) {
            throw new MemberCategoryHandler(ErrorStatus.MEMBER_CATEGORY_NOT_FOUND);
        }

        List<Schedule> scheduleList = memberCategoryList.stream()
                .map(memberCategory -> scheduleRepository.findByMemberCategoryAndMonth(memberCategory, firstDayOfMonth, lastDayOfMonth))
                .flatMap(List::stream)  // List<Schedule>을 평탄화하여 하나의 스트림으로 변환
                .collect(Collectors.toList());

        if (scheduleList.isEmpty()) {
            throw new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_EXIST);
        }

        // 날짜별로 그룹화
        Map<LocalDate, List<Schedule>> groupedByDate = scheduleList.stream()
                .collect(Collectors.groupingBy(Schedule::getDate));

        // 날짜순으로 정렬
        List<LocalDate> sortedDates = groupedByDate.keySet().stream()
                .sorted()
                .collect(Collectors.toList());

        return sortedDates.stream()
                .map(date -> {
                    List<Schedule> schedulesForDate = groupedByDate.get(date);
                    return ScheduleConverter.toScheduleByMonthDto(schedulesForDate, date);
                })
                .collect(Collectors.toList());
    }
}
