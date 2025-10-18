package com.example.ohmobackend.service.scheduleService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.MemberCategoryHandler;
import com.example.ohmobackend.apiPayload.exception.handler.ScheduleHandler;
import com.example.ohmobackend.converter.ScheduleConverter;
import com.example.ohmobackend.domain.*;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.repository.MemberCategoryRepository;
import com.example.ohmobackend.repository.RoutineRepository;
import com.example.ohmobackend.repository.ScheduleRepository;
import com.example.ohmobackend.repository.TodoRepository;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleQueryServiceImpl implements ScheduleQueryService {

    final MemberCategoryRepository memberCategoryRepository;
    final ScheduleRepository scheduleRepository;
    final TodoRepository todoRepository;
    final RoutineRepository routineRepository;

    @Override
    public ScheduleResponseDto.ScheduleDto getScheduleList(LocalDate date, Member member) {
        // 투두 찾기
        List<Schedule> todoScheduleList = scheduleRepository.findSchedulesByMemberAndDateAndScheduleType(member, date, ScheduleType.TO_DO);
        List<ScheduleResponseDto.ScheduleTodoDto> scheduleTodoList = todoScheduleList.stream()
                .map(todoSchedule -> ScheduleConverter.toScheduleTodoDto(todoSchedule, todoSchedule.getTodo()))
                .collect(Collectors.toList());

        // 루틴 찾기
        List<Routine> routineList = routineRepository.findRoutinesWithScheduleByMemberAndDate(member, date);
        List<ScheduleResponseDto.ScheduleWithRoutineListDto> scheduleRoutineList = routineList.stream()
                .map(routine -> ScheduleConverter.toScheduleWithRoutineListDto(routine.getSchedule(), routineList))
                .collect(Collectors.toList());

        return ScheduleConverter.toScheduleDto(scheduleTodoList, scheduleRoutineList);

    }

    @Override
    public List<ScheduleResponseDto.ScheduleTodoDto> getCompleteTodoList(LocalDate date, Member member) {
        // 투두 찾기
        List<Todo> todoList = todoRepository.findTodosWithScheduleByMemberAndDateAndStatus(member, date, true);
        List<ScheduleResponseDto.ScheduleTodoDto> scheduleTodoList = todoList.stream()
                .map(todo -> ScheduleConverter.toScheduleTodoDto(todo.getSchedule(), todo))
                .collect(Collectors.toList());

        if (scheduleTodoList.isEmpty()) {
            throw new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_EXIST);
        }

        return scheduleTodoList;
    }

    @Override
    public List<ScheduleResponseDto.ScheduleByMonthDto> getScheduleListByMonth(String yearMonth, Member member) {
        LocalDate firstDayOfMonth = YearMonth.parse(yearMonth).atDay(1);
        LocalDate lastDayOfMonth = YearMonth.parse(yearMonth).atEndOfMonth();

        // 투두 찾기
        List<MemberCategory> todoMemberCategoryList = memberCategoryRepository.findByMemberAndScheduleType(member, ScheduleType.TO_DO);

        if (todoMemberCategoryList.isEmpty()) {
            throw new MemberCategoryHandler(ErrorStatus.MEMBER_CATEGORY_NOT_FOUND);
        }

        List<Schedule> scheduleList = todoMemberCategoryList.stream()
                .map(memberCategory -> scheduleRepository.findByMemberCategoryAndMonth(memberCategory, firstDayOfMonth, lastDayOfMonth))
                .flatMap(List::stream)  // List<Schedule>을 평탄화하여 하나의 스트림으로 변환
                .collect(Collectors.toList());

        Map<LocalDate, List<Schedule>> dateToSchedulesMap = scheduleList.stream()
                .collect(Collectors.groupingBy(Schedule::getDate));

        // 루틴 찾기
        List<Routine> routineList = routineRepository.findRoutinesByMemberAndMonth(member, firstDayOfMonth, lastDayOfMonth);
        routineList.forEach(routine -> {
            LocalDate date = routine.getDate();
            Schedule schedule = routine.getSchedule();

            dateToSchedulesMap
                    .computeIfAbsent(date, k -> new ArrayList<>())
                    .add(schedule);
        });

        // 날짜순으로 정렬
        List<LocalDate> sortedDates = dateToSchedulesMap.keySet().stream()
                .sorted()
                .collect(Collectors.toList());

        return sortedDates.stream()
                .map(date -> {
                    List<Schedule> schedulesForDate = dateToSchedulesMap.get(date);
                    return ScheduleConverter.toScheduleByMonthDto(schedulesForDate, date);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleResponseDto.ScheduleDto getScheduleListByKeyword(String keyword, Member member) {
        List<MemberCategory> memberCategoryList = memberCategoryRepository.findByMember(member);

        if (memberCategoryList.isEmpty()) {
            throw new MemberCategoryHandler(ErrorStatus.MEMBER_CATEGORY_NOT_FOUND);
        }

        List<Schedule> scheduleList = memberCategoryList.stream()
                .map(memberCategory -> scheduleRepository.findByMemberCategoryAndTitleContaining(memberCategory, keyword))
                .flatMap(List::stream)  // List<Schedule>을 평탄화하여 하나의 스트림으로 변환
                .sorted(Comparator.comparing(Schedule::getDate))
                .collect(Collectors.toList());

        if (scheduleList.isEmpty()) {
            throw new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_EXIST);
        }

        // 투두
        List<ScheduleResponseDto.ScheduleTodoDto> scheduleTodoList = scheduleList.stream()
                .map(schedule -> ScheduleConverter.toScheduleTodoDto(schedule, schedule.getTodo()))
                .collect(Collectors.toList());

        // 루틴
        List<ScheduleResponseDto.ScheduleWithRoutineListDto> scheduleRoutineList = scheduleList.stream()
                .map(schedule -> ScheduleConverter.toScheduleWithRoutineListDto(schedule, schedule.getRoutineList()))
                .collect(Collectors.toList());

        return ScheduleConverter.toScheduleDto(scheduleTodoList, scheduleRoutineList);
    }


    // 데이 로그 화면에서 루틴 완료 상태 조회
//    @Override
//    public List<ScheduleResponseDto.RoutineStatusByContentDto> getRoutineStatusList(LocalDate startDate, LocalDate endDate, Member member) {
//        List<MemberCategory> memberCategoryList = memberCategoryRepository.findByMemberAndScheduleType(member, ScheduleType.ROUTINE);
//
//        if (memberCategoryList.isEmpty()) {
//            throw new MemberCategoryHandler(ErrorStatus.MEMBER_CATEGORY_ROUTINE_NOT_FOUND);
//        }
//
//        List<Schedule> scheduleList = memberCategoryList.stream()
//                .map(memberCategory -> scheduleRepository.findByMemberCategoryAndDateBetween(memberCategory, startDate, endDate))
//                .flatMap(List::stream)  // List<Schedule>을 평탄화하여 하나의 스트림으로 변환
//                .collect(Collectors.toList());
//
//        if (scheduleList.isEmpty()) {
//            throw new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_EXIST);
//        }
//
//        // 루틴 이름별로 그룹화
//        Map<String, List<Schedule>> groupedByContent = scheduleList.stream()
//                .collect(Collectors.groupingBy(
//                        Schedule::getContent,
//                        Collectors.collectingAndThen(
//                                Collectors.toList(),
//                                list -> list.stream()
//                                        .sorted(Comparator.comparing(Schedule::getDate))
//                                        .collect(Collectors.toList())
//                        )
//                ));
//
//        List<String> sortedContents = groupedByContent.keySet().stream()
//                .sorted()
//                .collect(Collectors.toList());
//
//        return sortedContents.stream()
//                .map(content -> {
//                    List<Schedule> schedulesForContent = groupedByContent.get(content);
//                    return ScheduleConverter.toRoutineStatusByContentDto(schedulesForContent, content);
//                })
//                .collect(Collectors.toList());
//    }

    // 데이로그 completion rate 조회
//    @Override
//    public List<ScheduleResponseDto.ScheduleCompletionRateByMonthDto> getScheduleCompletionReteByMonth(String yearMonth, Member member) {
//        LocalDate firstDayOfMonth = YearMonth.parse(yearMonth).atDay(1);
//        LocalDate lastDayOfMonth = YearMonth.parse(yearMonth).atEndOfMonth();
//
//        List<MemberCategory> memberCategoryList = memberCategoryRepository.findByMember(member);
//
//        if (memberCategoryList.isEmpty()) {
//            throw new MemberCategoryHandler(ErrorStatus.MEMBER_CATEGORY_NOT_FOUND);
//        }
//
//        List<Schedule> scheduleList = memberCategoryList.stream()
//                .map(memberCategory -> scheduleRepository.findByMemberCategoryAndMonth(memberCategory, firstDayOfMonth, lastDayOfMonth))
//                .flatMap(List::stream)  // List<Schedule>을 평탄화하여 하나의 스트림으로 변환
//                .collect(Collectors.toList());
//
//        if (scheduleList.isEmpty()) {
//            throw new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_EXIST);
//        }
//
//        // 날짜별로 그룹화
//        Map<LocalDate, List<Schedule>> groupedByDate = scheduleList.stream()
//                .collect(Collectors.groupingBy(Schedule::getDate));
//
//        Map<LocalDate, Double> completionRateByDate = groupedByDate.entrySet().stream()
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        entry -> {
//                            List<Schedule> schedules = entry.getValue();
//                            long total = schedules.size();
//                            long completed = schedules.stream().filter(Schedule::isStatus).count();
//                            return total == 0 ? 0.0 : Math.round(((double) completed / total) * 100 * 100.0) / 100.0;
//                        }
//                ));
//
//        // 날짜순으로 정렬
//        List<LocalDate> sortedDates = groupedByDate.keySet().stream()
//                .sorted()
//                .collect(Collectors.toList());
//
//        return sortedDates.stream()
//                .map(date -> {
//                    double rate = completionRateByDate.get(date);
//                    System.out.println(date+":"+rate);
//                    return ScheduleConverter.toScheduleCompletionRateByMonthDto(date, rate);
//                })
//                .collect(Collectors.toList());
//    }
}
