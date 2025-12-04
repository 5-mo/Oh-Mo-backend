package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.groupService.GroupCommandService;
import com.example.ohmobackend.service.groupService.GroupQueryService;
import com.example.ohmobackend.service.groupService.GroupQueryServiceImpl;
import com.example.ohmobackend.web.dto.groupDto.GroupRequestDto;
import com.example.ohmobackend.web.dto.groupDto.GroupResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/group")
public class GroupController {

    private final GroupCommandService groupCommandService;
    private final GroupQueryService groupQueryService;

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

    @GetMapping("/member")
    @Operation(summary = "그룹 멤버 조회 API", description = "그룹 멤버 조회 API 입니다.")
    public ApiResponse<GroupResponseDto.GroupMembersDto> getGroupMember(
            @RequestParam(name = "groupId") Long groupId, @AuthUser Member member) {
        GroupResponseDto.GroupMembersDto responseDto = groupQueryService.getGroupMembers(groupId, member);
        return ApiResponse.onSuccess(SuccessStatus.GROUP_REGISTER_OK, responseDto);
    }
}
