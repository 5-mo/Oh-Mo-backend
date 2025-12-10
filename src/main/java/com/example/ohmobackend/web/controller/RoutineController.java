package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.routineService.RoutineCommandService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/routine")
@Slf4j
public class RoutineController {

    final RoutineCommandService routineCommandService;

    @PatchMapping("/{routineId}")
    @Operation(summary = "루틴 상태 변경 API", description = "상태 변경 API 입니다.")
    public ApiResponse<Object> updateRoutineStatus(@PathVariable(name = "routineId") Long routineId, @AuthUser Member member) {
        routineCommandService.updateRoutineStatus(routineId, member);
        return ApiResponse.onSuccess(SuccessStatus.ROUTINE_UPDATE_STATUS_OK, null);
    }

    @DeleteMapping("/{routineId}")
    @Operation(summary = "루틴 삭제 API", description = "루틴 삭제 API 입니다.")
    public ApiResponse<Object> deleteRoutine(@PathVariable(name = "routineId") Long routineId, @AuthUser Member member) {
        routineCommandService.deleteRoutine(routineId, member);
        return ApiResponse.onSuccess(SuccessStatus.ROUTINE_DELETE_OK, null);
    }
}
