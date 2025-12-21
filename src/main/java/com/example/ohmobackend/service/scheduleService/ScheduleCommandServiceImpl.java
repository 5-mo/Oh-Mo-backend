package com.example.ohmobackend.service.scheduleService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.MemberCategoryHandler;
import com.example.ohmobackend.apiPayload.exception.handler.MemberHandler;
import com.example.ohmobackend.apiPayload.exception.handler.ScheduleHandler;
import com.example.ohmobackend.converter.RoutineConverter;
import com.example.ohmobackend.converter.ScheduleConverter;
import com.example.ohmobackend.converter.TodoConverter;
import com.example.ohmobackend.domain.*;
import com.example.ohmobackend.domain.enums.ScheduleType;
import com.example.ohmobackend.repository.*;
import com.example.ohmobackend.web.dto.routineDto.RoutineResponseDto;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleRequestDto;
import com.example.ohmobackend.web.dto.scheduleDto.ScheduleResponseDto;
import com.example.ohmobackend.web.dto.todoDto.TodoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.example.ohmobackend.service.scheduleService.DateCalculator.getDatesFromNowDate;


@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleCommandServiceImpl implements ScheduleCommandService {

    final private MemberCategoryRepository memberCategoryRepository;
    final private ScheduleRepository scheduleRepository;
    final private TodoRepository todoRepository;
    final private RoutineRepository routineRepository;

    @Override
    public List<RoutineResponseDto.RoutineDto> addRoutine(
            ScheduleRequestDto.AddRequestDto requestDto,
            Member member) {
        MemberCategory memberCategory = getMemberCategory(requestDto, member, ScheduleType.ROUTINE);

        validateMemberCategory(memberCategory, member, ScheduleType.ROUTINE);

        Schedule schedule = ScheduleConverter.toEntity(requestDto, memberCategory);

        // repeatWeek 저장
        if (requestDto.getRoutineWeek() == null || requestDto.getRoutineWeek().isEmpty()) {
            throw new ScheduleHandler(ErrorStatus.REPEAT_WEEK_IS_EMPTY);
        }
        schedule.getRepeatWeek().addAll(requestDto.getRoutineWeek());

        // DB에 저장
        scheduleRepository.save(schedule);

        // 반복 요일 기반 Routine 생성
        List<LocalDate> dates = getDatesFromNowDate(requestDto.getDate(), requestDto.getRoutineWeek()); // 반복 요일에 해당하는 날짜 리스트

        List<Routine> routineList = dates.stream()
                .map(date -> RoutineConverter.toEntity(schedule, date))
                .collect(Collectors.toList());

        routineRepository.saveAll(routineList);
        return routineList.stream()
                .map(RoutineConverter::toRoutineDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<RoutineResponseDto.RoutineDto> updateRoutine(
            Long scheduleId,
            ScheduleRequestDto.AddRequestDto requestDto,
            Member member
    ) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        MemberCategory memberCategory = schedule.getMemberCategory();
        validateMemberCategory(memberCategory, member, ScheduleType.ROUTINE);

        boolean routineChanged = applySchedulePatch(schedule, requestDto, ScheduleType.ROUTINE);

        if (routineChanged) {
            return updateRoutinesIncrementally(schedule);
        }

        return findRoutineDtos(schedule);
    }

    @Override
    public TodoResponseDto.TodoDto addTodo(ScheduleRequestDto.AddRequestDto requestDto, Member member) {
        MemberCategory memberCategory = getMemberCategory(requestDto, member, ScheduleType.TO_DO);
        validateMemberCategory(memberCategory, member, ScheduleType.TO_DO);
        Schedule schedule = scheduleRepository.save(ScheduleConverter.toEntity(requestDto, memberCategory));
        Todo todo = todoRepository.save(TodoConverter.toEntity(schedule));
        return TodoConverter.toTodoDto(todo);
    }

    @Override
    public TodoResponseDto.TodoDto updateTodo(Long scheduleId, ScheduleRequestDto.AddRequestDto requestDto, Member member) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        MemberCategory memberCategory = schedule.getMemberCategory();
        validateMemberCategory(memberCategory, member, ScheduleType.TO_DO);
        applySchedulePatch(schedule, requestDto, ScheduleType.TO_DO);
        Todo todo = todoRepository.findBySchedule(schedule);
        return TodoConverter.toTodoDto(todo);
    }

    @Override
    @Transactional
    public ScheduleResponseDto.ScheduleTodoDto updateScheduleDate(ScheduleRequestDto.UpdateTodoDateRequestDto requestDto, Member member) {
        Schedule schedule = scheduleRepository.findById(requestDto.getScheduleId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        MemberCategory memberCategory = schedule.getMemberCategory();
        validateMemberCategory(memberCategory, member, ScheduleType.TO_DO);
        schedule.updateDate(requestDto.getDate());

        return ScheduleConverter.toScheduleTodoDto(schedule, schedule.getTodo());
    }

    @Override
    public void updateScheduleAlarmTime(ScheduleRequestDto.UpdateScheduleAlarmTimeDto requestDto, Member member) {
        Schedule schedule = scheduleRepository.findById(requestDto.getScheduleId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));

        if (schedule.getMemberCategory().getMember() != member) {
            throw new MemberHandler(ErrorStatus.INVALID_MEMBER);
        }

        schedule.updateAlarmTime(requestDto.getAlarmTime());
    }

    private MemberCategory getMemberCategory(ScheduleRequestDto.AddRequestDto requestDto, Member member, ScheduleType scheduleType) {
        MemberCategory memberCategory = (requestDto.getCategoryId() == null)
                ? memberCategoryRepository.findByMemberAndCategoryNameAndScheduleType(member, "default", scheduleType)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.DEFAULT_MEMBER_CATEGORY_NOT_FOUND))
                : memberCategoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.MEMBER_CATEGORY_NOT_FOUND));
        return memberCategory;
    }

    private void validateMemberCategory(MemberCategory memberCategory, Member member, ScheduleType scheduleType) {
        if (!member.equals(memberCategory.getMember())) {
            throw new MemberCategoryHandler(ErrorStatus.INVALID_MEMBER_CATEGORY);
        }

        if (memberCategory.getScheduleType() != scheduleType) {
            throw new MemberCategoryHandler(ErrorStatus.MEMBER_CATEGORY_INVALID_SCHEDULE_TYPE);
        }
    }

    private boolean applySchedulePatch(
            Schedule schedule,
            ScheduleRequestDto.AddRequestDto dto,
            ScheduleType scheduleType
    ) {
        boolean routineChanged = false;

        routineChanged |= patchIfPresent(dto.getDate(), schedule::updateDate);
        routineChanged |= patchIfPresent(dto.getRoutineWeek(),
                weeks -> schedule.updateRepeatWeek(new HashSet<>(weeks)));

        patchIfPresent(dto.getTime(), schedule::updateTime);
        patchIfPresent(dto.getAlarmTime(), schedule::updateAlarmTime);
        patchIfPresent(dto.getContent(), schedule::updateContent);
        patchIfPresent(dto.getCategoryId(),
                memberCategoryId -> changeMemberCategory(schedule, memberCategoryId, scheduleType));
        return routineChanged;
    }

    private void changeMemberCategory(Schedule schedule, Long memberCategoryId, ScheduleType scheduleType) {
        MemberCategory memberCategory = memberCategoryRepository.findById(memberCategoryId)
                .orElseThrow(() -> new MemberCategoryHandler(ErrorStatus.MEMBER_CATEGORY_NOT_FOUND));

        if (memberCategory.getScheduleType() != scheduleType) {
            throw new MemberCategoryHandler(ErrorStatus.MEMBER_CATEGORY_INVALID_SCHEDULE_TYPE);
        }

        schedule.changeMemberCategory(memberCategory);
    }

    private <T> boolean patchIfPresent(T value, Consumer<T> updater) {
        if (value == null) {
            return false;
        }
        updater.accept(value);
        return true;
    }

    private List<RoutineResponseDto.RoutineDto> updateRoutinesIncrementally(Schedule schedule) {

        Set<LocalDate> existingDates = findExistingRoutineDates(schedule);
        Set<LocalDate> newDates = calculateNewRoutineDates(schedule);

        deleteObsoleteRoutines(schedule, newDates);
        addMissingRoutines(schedule, existingDates, newDates);

        return findRoutineDtos(schedule);
    }

    private Set<LocalDate> findExistingRoutineDates(Schedule schedule) {
        return routineRepository.findAllBySchedule(schedule).stream()
                .map(Routine::getDate)
                .collect(Collectors.toSet());
    }

    private Set<LocalDate> calculateNewRoutineDates(Schedule schedule) {
        return new HashSet<>(
                getDatesFromNowDate(
                        schedule.getDate(),
                        schedule.getRepeatWeek()
                )
        );
    }

    private void deleteObsoleteRoutines(Schedule schedule, Set<LocalDate> newDates) {
        List<Routine> toDelete = routineRepository.findAllBySchedule(schedule).stream()
                .filter(routine -> !newDates.contains(routine.getDate()))
                .toList();

        routineRepository.deleteAll(toDelete);
    }

    private void addMissingRoutines(
            Schedule schedule,
            Set<LocalDate> existingDates,
            Set<LocalDate> newDates
    ) {
        List<Routine> toAdd = newDates.stream()
                .filter(date -> !existingDates.contains(date))
                .map(date -> RoutineConverter.toEntity(schedule, date))
                .toList();

        routineRepository.saveAll(toAdd);
    }

    private List<RoutineResponseDto.RoutineDto> findRoutineDtos(Schedule schedule) {
        return routineRepository.findAllBySchedule(schedule).stream()
                .map(RoutineConverter::toRoutineDto)
                .toList();
    }

}
