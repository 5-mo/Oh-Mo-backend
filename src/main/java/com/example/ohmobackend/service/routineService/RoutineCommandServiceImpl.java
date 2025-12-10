package com.example.ohmobackend.service.routineService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.MemberHandler;
import com.example.ohmobackend.apiPayload.exception.handler.ScheduleHandler;
import com.example.ohmobackend.converter.RoutineConverter;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.Routine;
import com.example.ohmobackend.repository.RoutineRepository;
import com.example.ohmobackend.web.dto.routineDto.RoutineResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class RoutineCommandServiceImpl implements RoutineCommandService {

    final RoutineRepository routineRepository;

    @Override
    @Transactional
    public RoutineResponseDto.RoutineDto updateRoutineStatus(Long routineId, Member member) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));

        if(routine.getSchedule().getMemberCategory().getMember() != member) {
            throw new MemberHandler(ErrorStatus.INVALID_MEMBER);
        }

        routine.updateStatus(!routine.isStatus());
        return RoutineConverter.toRoutineDto(routine);
    }

    @Override
    @Transactional
    public void deleteRoutine(Long routineId, Member member) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));

        if(routine.getSchedule().getMemberCategory().getMember() != member) {
            throw new MemberHandler(ErrorStatus.INVALID_MEMBER);
        }

        routineRepository.delete(routine);
    }
}
