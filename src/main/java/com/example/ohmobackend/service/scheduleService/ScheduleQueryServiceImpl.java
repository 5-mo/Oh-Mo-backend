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
import java.util.stream.Stream;

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

        Map<Schedule, List<Routine>> scheduleToRoutines = routineList.stream()
                .filter(r -> r.getSchedule().getScheduleType() == ScheduleType.ROUTINE)
                .collect(Collectors.groupingBy(Routine::getSchedule));

        List<ScheduleResponseDto.ScheduleWithRoutineListDto> scheduleRoutineList = scheduleToRoutines.entrySet().stream()
                .map(entry -> ScheduleConverter.toScheduleWithRoutineListDto(entry.getKey(), entry.getValue()))
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
        List<Routine> routineList = routineRepository.findRoutinesByMemberAndDate(member, firstDayOfMonth, lastDayOfMonth);
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
    @Override
    public List<ScheduleResponseDto.ScheduleWithRoutineListDto> getRoutineStatusList(LocalDate startDate, LocalDate endDate, Member member) {
        // 루틴 찾기
        List<Routine> routineList = routineRepository.findRoutinesByMemberAndDate(member, startDate, endDate);

        Map<Schedule, List<Routine>> scheduleToRoutines = routineList.stream()
                .filter(r -> r.getSchedule().getScheduleType() == ScheduleType.ROUTINE)
                .collect(Collectors.groupingBy(Routine::getSchedule));

        List<ScheduleResponseDto.ScheduleWithRoutineListDto> scheduleRoutineList = scheduleToRoutines.entrySet().stream()
                .map(entry -> ScheduleConverter.toScheduleWithRoutineListDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return scheduleRoutineList;
    }

    // 데이로그 completion rate 조회
    @Override
    public List<ScheduleResponseDto.ScheduleCompletionRateByMonthDto> getScheduleCompletionReteByMonth(String yearMonth, Member member) {
        LocalDate firstDayOfMonth = YearMonth.parse(yearMonth).atDay(1);
        LocalDate lastDayOfMonth = YearMonth.parse(yearMonth).atEndOfMonth();

        // TO_DO 카테고리 가져오기
        List<MemberCategory> todoMemberCategoryList = memberCategoryRepository.findByMemberAndScheduleType(member, ScheduleType.TO_DO);
        if (todoMemberCategoryList.isEmpty()) {
            throw new MemberCategoryHandler(ErrorStatus.MEMBER_CATEGORY_NOT_FOUND);
        }

        // 월별 Schedule (Todo용)
        List<Schedule> scheduleList = todoMemberCategoryList.stream()
                .map(cat -> scheduleRepository.findByMemberCategoryAndMonth(cat, firstDayOfMonth, lastDayOfMonth))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        // 월별 Routine
        List<Routine> routineList = routineRepository.findRoutinesByMemberAndDate(member, firstDayOfMonth, lastDayOfMonth);

        // 날짜별 Todo + Routine 모두 합치기
        Map<LocalDate, Map<String, List<?>>> dateToItemsMap = new HashMap<>();

        // 일정(TO_DO) 추가
        for (Schedule schedule : scheduleList) {
            LocalDate date = schedule.getDate();
            dateToItemsMap
                    .computeIfAbsent(date, k -> new HashMap<>())
                    .computeIfAbsent("todoList", k -> new ArrayList<Schedule>());
            ((List<Schedule>) dateToItemsMap.get(date).get("todoList")).add(schedule);
        }

        // 루틴 추가
        for (Routine routine : routineList) {
            LocalDate date = routine.getDate();
            dateToItemsMap
                    .computeIfAbsent(date, k -> new HashMap<>())
                    .computeIfAbsent("routineList", k -> new ArrayList<Routine>());
            ((List<Routine>) dateToItemsMap.get(date).get("routineList")).add(routine);
        }

        List<ScheduleResponseDto.ScheduleCompletionRateByMonthDto> result =
                dateToItemsMap.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .map(entry -> {
                            LocalDate date = entry.getKey();
                            Map<String, List<?>> items = entry.getValue();

                            List<Schedule> todos = (List<Schedule>) items.getOrDefault("todoList", Collections.emptyList());
                            List<Routine> routines = (List<Routine>) items.getOrDefault("routineList", Collections.emptyList());

                            long totalCount = todos.size() + routines.size();

                            if (totalCount == 0) {
                                return ScheduleConverter.toScheduleCompletionRateByMonthDto(date, 0);
                            }

                            long completedCount = Stream.concat(
                                    todos.stream()
                                            .filter(schedule -> schedule.getTodo() != null && schedule.getTodo().isStatus()),
                                    routines.stream()
                                            .filter(Routine::isStatus)
                            ).count();

                            double completionRate = (double) completedCount / totalCount * 100;

                            return ScheduleConverter.toScheduleCompletionRateByMonthDto(date, completionRate);
                        })
                        .collect(Collectors.toList());


        return result;
    }
}
