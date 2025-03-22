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
import java.util.List;
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

        System.out.println(memberCategoryList.get(0).getId());

        List<Schedule> scheduleList = memberCategoryList.stream()
                .map(memberCategory -> {
                    List<Schedule> schedules = scheduleRepository.findByMemberCategoryAndDate(memberCategory, date);
                    if (schedules.isEmpty()) {
                        System.out.println(date);
                        throw new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_EXIST);
                    }
                    return schedules;
                })
                .flatMap(List::stream)  // List<Schedule>을 평탄화하여 하나의 스트림으로 변환
                .collect(Collectors.toList());

        return scheduleList.stream()
                .map(schedule -> ScheduleConverter.toScheduleDto(schedule))
                .collect(Collectors.toList());
    }
}
