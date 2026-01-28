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
    public ScheduleResponseDto.ScheduleByDateDto getScheduleList(LocalDate date, Member member) {
        // 투두 찾기
        List<Todo> todos = todoRepository.findTodosWithScheduleByMemberAndDate(member, date);
        List<ScheduleResponseDto.ScheduleTodoDto> scheduleTodoList = getTodoDtosByDate(todos);

        // 루틴 찾기
        List<Routine> routineList = routineRepository.findRoutinesWithScheduleByMemberAndDate(member, date);
        List<ScheduleResponseDto.ScheduleRoutineDto> scheduleRoutineList = getRoutineDtosByDate(routineList);
        return ScheduleConverter.toScheduleByDateDto(scheduleTodoList, scheduleRoutineList);
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
        YearMonth ym = YearMonth.parse(yearMonth);
        LocalDate firstDayOfMonth = ym.atDay(1);
        LocalDate lastDayOfMonth = ym.atEndOfMonth();

        // 투두 찾기
        List<Schedule> scheduleWithTodos = getTodoSchedulesByMonth(member, firstDayOfMonth, lastDayOfMonth);
        List<Routine> routines = routineRepository.findRoutinesByMemberAndDate(member, firstDayOfMonth, lastDayOfMonth);


        Map<LocalDate, List<Schedule>> dateMap = scheduleWithTodos.stream()
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

    private List<ScheduleResponseDto.ScheduleRoutineDto> getRoutineDtosByDate(List<Routine> routineList) {
        return routineList.stream()
                .filter(r -> r.getSchedule().getScheduleType() == ScheduleType.ROUTINE)
                .map(r -> ScheduleConverter.toScheduleRoutineDto(r.getSchedule(), r))
                .toList();
    }

    private List<ScheduleResponseDto.ScheduleTodoDto> getTodoDtosByDate(List<Todo> todos) {
        List<ScheduleResponseDto.ScheduleTodoDto> scheduleTodoList = todos.stream()
                .map(todo -> ScheduleConverter.toScheduleTodoDto(todo.getSchedule(), todo))
                .collect(Collectors.toList());
        return scheduleTodoList;
    }

    @Override
    public ScheduleResponseDto.ScheduleByKeyWordDto getScheduleListByKeyword(String keyword, Member member) {
        List<MemberCategory> memberCategoryList = memberCategoryRepository.findByMember(member);

        if (memberCategoryList.isEmpty()) {
            throw new MemberCategoryHandler(ErrorStatus.MEMBER_CATEGORY_NOT_FOUND);
        }

        // 투두
        List<Todo> todos = memberCategoryList.stream()
                .map(memberCategory -> todoRepository.findByMemberCategoryAndTitleContaining(memberCategory, keyword))
                .flatMap(List::stream)
                .sorted(Comparator.comparing(t -> t.getSchedule().getDate()))
                .collect(Collectors.toList());
        List<ScheduleResponseDto.ScheduleTodoDto> scheduleTodoList = getTodoDtosByDate(todos);

        // 루틴
        List<Schedule> schedules = memberCategoryList.stream()
                .map(memberCategory -> scheduleRepository.findByMemberCategoryAndTitleContaining(memberCategory, keyword))
                .flatMap(List::stream)
                .sorted(Comparator.comparing(Schedule::getDate))
                .collect(Collectors.toList());
        List<ScheduleResponseDto.ScheduleWithRoutineListDto> scheduleRoutineList = schedules.stream()
                .map(schedule -> ScheduleConverter.toScheduleWithRoutineListDto(schedule, schedule.getRoutineList()))
                .collect(Collectors.toList());

        return ScheduleConverter.toScheduleByKeywordDto(scheduleTodoList, scheduleRoutineList);
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
}
