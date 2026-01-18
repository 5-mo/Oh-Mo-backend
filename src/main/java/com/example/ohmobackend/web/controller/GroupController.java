package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.groupService.GroupCommandService;
import com.example.ohmobackend.service.groupService.GroupQueryService;
import com.example.ohmobackend.web.dto.groupDto.GroupRequestDto;
import com.example.ohmobackend.web.dto.groupDto.GroupResponseDto;
import com.example.ohmobackend.web.dto.memberGroupDto.MemberGroupResponseDto;
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

    @PatchMapping("/nickname")
    @Operation(summary = "그룹 닉네임 업데이트 API", description = "그룹 닉네임 업데이트 API 입니다.")
    public ApiResponse<MemberGroupResponseDto.MemberGroupInfoDto> updateNickname(
            @RequestBody GroupRequestDto.AddGroupNicknameDto request, @AuthUser Member member) {
        MemberGroupResponseDto.MemberGroupInfoDto memberGroupInfoDto = groupCommandService.updateNickname(member, request);
        return ApiResponse.onSuccess(SuccessStatus.UPDATE_GROUP_NICKNAME_OK, memberGroupInfoDto);
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

    @DeleteMapping("")
    @Operation(summary = "그룹 삭제 API", description = "그룹 삭제 API 입니다.")
    public ApiResponse<Object> deleteGroup(@AuthUser Member member, @RequestBody GroupRequestDto.DeleteGroupRequestDto request) {
        groupQueryService.deleteGroup(member, request);
        return ApiResponse.onSuccess(SuccessStatus.GROUP_DELETE_OK, null);
    }
}
