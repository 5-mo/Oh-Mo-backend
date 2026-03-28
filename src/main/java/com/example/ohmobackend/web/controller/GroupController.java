package com.example.ohmobackend.web.controller;

import com.example.ohmobackend.apiPayload.ApiResponse;
import com.example.ohmobackend.apiPayload.code.status.SuccessStatus;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.security.handler.AuthUser;
import com.example.ohmobackend.service.SseService;
import com.example.ohmobackend.service.groupService.GroupCommandService;
import com.example.ohmobackend.service.groupService.GroupQueryService;
import com.example.ohmobackend.web.dto.groupDto.GroupRequestDto;
import com.example.ohmobackend.web.dto.groupDto.GroupResponseDto;
import com.example.ohmobackend.web.dto.memberGroupDto.MemberGroupResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/group")
public class GroupController {

    private final GroupCommandService groupCommandService;
    private final GroupQueryService groupQueryService;
    private final SseService sseService;

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

    @DeleteMapping("/leave")
    @Operation(summary = "그룹 나가기 API", description = "그룹 나가기 API 입니다. 매니저는 다른 멤버가 있을 경우 나갈 수 없습니다.")
    public ApiResponse<Object> leaveGroup(@AuthUser Member member, @RequestBody GroupRequestDto.LeaveGroupRequestDto request) {
        groupCommandService.leaveGroup(member, request);
        return ApiResponse.onSuccess(SuccessStatus.GROUP_LEAVE_OK, null);
    }

    @DeleteMapping("/kick")
    @Operation(summary = "그룹 멤버 강퇴 API", description = "방장이 특정 멤버를 그룹에서 강퇴합니다.")
    public ApiResponse<Object> kickMember(@AuthUser Member member, @RequestBody GroupRequestDto.KickMemberRequestDto request) {
        groupCommandService.kickMember(member, request);
        return ApiResponse.onSuccess(SuccessStatus.GROUP_KICK_MEMBER_OK, null);
    }

    @PatchMapping("/manager")
    @Operation(summary = "방장 넘기기 API", description = "현재 방장이 다른 멤버에게 방장을 넘깁니다.")
    public ApiResponse<Object> transferManager(@AuthUser Member member, @RequestBody GroupRequestDto.TransferManagerRequestDto request) {
        groupCommandService.transferManager(member, request);
        return ApiResponse.onSuccess(SuccessStatus.GROUP_TRANSFER_MANAGER_OK, null);
    }

    @PostMapping("/invite")
    @Operation(summary = "멤버 초대 API", description = "방장이 이메일로 특정 멤버를 그룹에 초대합니다. 초대받은 멤버에게 FCM 알림이 전송됩니다.")
    public ApiResponse<Object> inviteMember(@AuthUser Member member, @Valid @RequestBody GroupRequestDto.InviteMemberRequestDto request) {
        groupCommandService.inviteMember(member, request);
        return ApiResponse.onSuccess(SuccessStatus.GROUP_INVITE_MEMBER_OK, null);
    }

    @GetMapping("/invite")
    @Operation(summary = "받은 초대 목록 조회 API", description = "현재 로그인한 멤버가 받은 대기 중인 초대 목록을 조회합니다.")
    public ApiResponse<List<GroupResponseDto.InvitationDto>> getInvitations(@AuthUser Member member) {
        List<GroupResponseDto.InvitationDto> invitations = groupQueryService.getInvitations(member);
        return ApiResponse.onSuccess(SuccessStatus.GROUP_INVITATION_LIST_OK, invitations);
    }

    @PostMapping("/invite/accept")
    @Operation(summary = "그룹 초대 수락 API", description = "초대받은 멤버가 초대를 수락하면 그룹에 추가됩니다.")
    public ApiResponse<Object> acceptInvitation(@AuthUser Member member, @Valid @RequestBody GroupRequestDto.InvitationActionRequestDto request) {
        groupCommandService.acceptInvitation(member, request);
        return ApiResponse.onSuccess(SuccessStatus.GROUP_INVITATION_ACCEPT_OK, null);
    }

    @PostMapping("/invite/reject")
    @Operation(summary = "그룹 초대 거절 API", description = "초대받은 멤버가 초대를 거절합니다.")
    public ApiResponse<Object> rejectInvitation(@AuthUser Member member, @Valid @RequestBody GroupRequestDto.InvitationActionRequestDto request) {
        groupCommandService.rejectInvitation(member, request);
        return ApiResponse.onSuccess(SuccessStatus.GROUP_INVITATION_REJECT_OK, null);
    }

    @GetMapping(value = "/{groupId}/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "일정 구독 API", description = "해당 그룹의 일정 변경 사항을 실시간으로 수신합니다.")
    public SseEmitter subscribe(
            @PathVariable("groupId") Long groupId,
            @AuthUser Member member) {

        SseEmitter emitter = new SseEmitter(60L * 1000 * 60);
        sseService.subscribe(groupId, emitter, member);

        return emitter;
    }
}
