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
        List<ScheduleResponseDto.ScheduleTodoDto> scheduleTodoList = getTodoDtosByDate(todoScheduleList);

        // 루틴 찾기
        List<Routine> routineList = routineRepository.findRoutinesWithScheduleByMemberAndDate(member, date);
        List<ScheduleResponseDto.ScheduleWithRoutineListDto> scheduleRoutineList = getRoutineDtosByDate(routineList);
        return ScheduleConverter.toScheduleDto(scheduleTodoList, scheduleRoutineList);
    }

    @Override
    public List<ScheduleResponseDto.ScheduleTodoDto> getCompleteTodoList(LocalDate date, Member member) {
        // 투두 찾기
        List<Todo> todoList = todoRepository.findTodosWithScheduleByMemberAndDateAndStatus(member, date, true);
        List<ScheduleResponseDto.ScheduleTodoDto> scheduleTodoList = todoList.stream()
                .map(todo -> ScheduleConverter.toScheduleTodoDto(todo.getSchedule()))
                .collect(Collectors.toList());

        if (scheduleTodoList.isEmpty()) {
            throw new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_EXIST);
        }

        return scheduleTodoList;
    }

    @Override
    public List<ScheduleResponseDto.ScheduleByMonthDto> getScheduleListByMonth(String yearMonth, Member member) {
        YearMonth ym = YearMonth.parse(yearMonth);
        LocalDate firstDayOfMonth = ym.atDay(1);
        LocalDate lastDayOfMonth = ym.atEndOfMonth();

        // 투두 찾기
        List<Schedule> todos = getTodoSchedulesByMonth(member, firstDayOfMonth, lastDayOfMonth;
        List<Routine> routines = routineRepository.findRoutinesByMemberAndDate(member, firstDayOfMonth, lastDayOfMonth);


        Map<LocalDate, List<Schedule>> dateMap = todos.stream()
                .collect(Collectors.groupingBy(Schedule::getDate));

        routines.forEach(r ->
                dateMap.computeIfAbsent(r.getDate(), k -> new ArrayList<>()).add(r.getSchedule())
        );

        return dateMap.entrySet().stream()
                .map(entry -> ScheduleConverter.toScheduleByMonthDto(entry.getValue(), entry.getKey()))
                .collect(Collectors.toList());
    }

    private List<Schedule> getTodoSchedulesByMonth(Member member, LocalDate start, LocalDate end) {
        List<MemberCategory> categories = memberCategoryRepository.findByMemberAndScheduleType(member, ScheduleType.TO_DO);
        if (categories.isEmpty()) throw new MemberCategoryHandler(ErrorStatus.MEMBER_CATEGORY_NOT_FOUND);

        return categories.stream()
                .flatMap(cat -> scheduleRepository.findByMemberCategoryAndMonth(cat, start, end).stream())
                .toList();
    }

    private List<ScheduleResponseDto.ScheduleWithRoutineListDto> getRoutineDtosByDate(List<Routine> routineList) {
        return routineList.stream()
                .filter(r -> r.getSchedule().getScheduleType() == ScheduleType.ROUTINE)
                .collect(Collectors.groupingBy(Routine::getSchedule))
                .entrySet().stream()
                .map(e -> ScheduleConverter.toScheduleWithRoutineListDto(e.getKey(), e.getValue()))
                .toList();
    }

    private List<ScheduleResponseDto.ScheduleTodoDto> getTodoDtosByDate(List<Schedule> todoScheduleList) {
        List<ScheduleResponseDto.ScheduleTodoDto> scheduleTodoList = todoScheduleList.stream()
                .map(todoSchedule -> ScheduleConverter.toScheduleTodoDto(todoSchedule))
                .collect(Collectors.toList());
        return scheduleTodoList;
    }

    @Override
    public ScheduleResponseDto.ScheduleDto getScheduleListByKeyword(String keyword, Member member) {
        List<MemberCategory> memberCategoryList = memberCategoryRepository.findByMember(member);

        if (memberCategoryList.isEmpty()) {
            throw new MemberCategoryHandler(ErrorStatus.MEMBER_CATEGORY_NOT_FOUND);
        }

        List<Schedule> scheduleList = memberCategoryList.stream()
                .map(memberCategory -> scheduleRepository.findByMemberCategoryAndTitleContaining(memberCategory, keyword))
                .flatMap(List::stream)
                .sorted(Comparator.comparing(Schedule::getDate))
                .collect(Collectors.toList());

        if (scheduleList.isEmpty()) {
            throw new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_EXIST);
        }

        // 투두
        List<ScheduleResponseDto.ScheduleTodoDto> scheduleTodoList = getTodoDtosByDate(scheduleList);

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

        List<Schedule> scheduleList = getTodoSchedulesByMonth(member, firstDayOfMonth, lastDayOfMonth);
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


        return dateToItemsMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    Map<String, List<?>> items = entry.getValue();

                    List<Schedule> todos = (List<Schedule>) items.getOrDefault("todoList", Collections.emptyList());
                    List<Routine> routines = (List<Routine>) items.getOrDefault("routineList", Collections.emptyList());

                    double completionRate = calculateCompletionRate(todos, routines);
                    return ScheduleConverter.toScheduleCompletionRateByMonthDto(date, completionRate);
                })
                .collect(Collectors.toList());
    }

    private double calculateCompletionRate(List<Schedule> todos, List<Routine> routines) {
        long total = todos.size() + routines.size();
        if (total == 0) return 0;

        long completed = todos.stream().filter(t -> t.getTodo() != null && t.getTodo().isStatus()).count()
                + routines.stream().filter(Routine::isStatus).count();

        return (double) completed / total * 100;
    }
}
