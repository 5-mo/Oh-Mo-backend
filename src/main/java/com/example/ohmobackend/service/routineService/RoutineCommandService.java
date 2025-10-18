package com.example.ohmobackend.service.routineService;

import com.example.ohmobackend.domain.Member;
import org.springframework.transaction.annotation.Transactional;

public interface RoutineCommandService {
    @Transactional
    void updateRoutineStatus(Long routineId, Member member);
}
