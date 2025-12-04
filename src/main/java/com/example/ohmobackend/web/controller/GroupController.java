package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.groupService.GroupCommandService;
import com.example.ohmobackend.service.groupService.GroupQueryService;
import com.example.ohmobackend.service.groupService.GroupQueryServiceImpl;
import com.example.ohmobackend.service.noticeService.NoticeCommandService;
import com.example.ohmobackend.web.dto.groupDto.GroupRequestDto;
import com.example.ohmobackend.web.dto.groupDto.GroupResponseDto;
import com.example.ohmobackend.web.dto.noticeDto.NoticeRequestDto;
import com.example.ohmobackend.web.dto.noticeDto.NoticeResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/group")
public class GroupController {

    private final GroupCommandService groupCommandService;
    private final GroupQueryService groupQueryService;
    private final NoticeCommandService noticeCommandService;

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
        return ApiResponse.onSuccess(SuccessStatus.GROUP_ENTER_OK, responseDto);
    }

    @PostMapping("/notice")
    @Operation(summary = "그룹 공지사항 등록 API", description = "그룹 공지사항 등록 API 입니다.")
    public ApiResponse<NoticeResponseDto.NoticeDto> enterGroup(
            @RequestBody NoticeRequestDto.AddNoticeDto request, @AuthUser Member member) {
        NoticeResponseDto.NoticeDto responseDto = noticeCommandService.addNotice(request, member);
        return ApiResponse.onSuccess(SuccessStatus.GROUP_ENTER_OK, responseDto);
    }

    @GetMapping("/member")
    @Operation(summary = "그룹 멤버 조회 API", description = "그룹 멤버 조회 API 입니다.")
    public ApiResponse<GroupResponseDto.GroupMembersDto> getGroupMember(
            @RequestParam(name = "groupId") Long groupId, @AuthUser Member member) {
        GroupResponseDto.GroupMembersDto responseDto = groupQueryService.getGroupMembers(groupId, member);
        return ApiResponse.onSuccess(SuccessStatus.GROUP_MEMBER_OK, responseDto);
    }

    @GetMapping("")
    @Operation(summary = "그룹 조회 API", description = "그룹 조회 API 입니다.")
    public ApiResponse<List<GroupResponseDto.GroupDto>> getGroups(@AuthUser Member member) {
        List<GroupResponseDto.GroupDto> responseDto = groupQueryService.getGroups(member);
        return ApiResponse.onSuccess(SuccessStatus.GROUP_FIND_OK, responseDto);
    }
}
