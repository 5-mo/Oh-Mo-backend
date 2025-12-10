package com.example.ohmobackend.service.routineService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.routineDto.RoutineResponseDto;
import org.springframework.transaction.annotation.Transactional;

public interface RoutineCommandService {
    @Transactional
    public RoutineResponseDto.RoutineDto updateRoutineStatus(Long routineId, Member member);

    @Transactional
    public void deleteRoutine(Long routineId, Member member);
}
