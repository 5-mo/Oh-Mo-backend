package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.groupService.GroupCommandService;
import com.example.ohmobackend.web.dto.groupDto.GroupRequestDto;
import com.example.ohmobackend.web.dto.groupDto.GroupResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/group")
public class GroupController {

    private final GroupCommandService groupCommandService;

    @PostMapping()
    @Operation(summary = "그룹 등록 API", description = "그룹 등록 API 입니다.")
    public ApiResponse<GroupResponseDto.GroupDto> addGroup(
            @RequestBody GroupRequestDto.AddGroupRequestDto request, @AuthUser Member member) {
        GroupResponseDto.GroupDto responseDto = groupCommandService.addGroup(member, request);
        return ApiResponse.onSuccess(SuccessStatus.GROUP_REGISTER_OK, responseDto);
    }

    @PostMapping("/enter")
    @Operation(summary = "그룹 들어가기 API", description = "그룹 들어가기 API 입니다.")
    public ApiResponse<GroupResponseDto.GroupDto> enterGroup(
            @RequestBody GroupRequestDto.EnterGroupRequestDto request, @AuthUser Member member) {
        GroupResponseDto.GroupDto responseDto = groupCommandService.enterGroup(member, request);
        return ApiResponse.onSuccess(SuccessStatus.GROUP_REGISTER_OK, responseDto);
    }
}
