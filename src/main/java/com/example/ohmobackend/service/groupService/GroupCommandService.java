package com.example.ohmobackend.service.groupService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.groupDto.GroupRequestDto;
import com.example.ohmobackend.web.dto.groupDto.GroupResponseDto;
import com.example.ohmobackend.web.dto.memberGroupDto.MemberGroupResponseDto;

public interface GroupCommandService {

    public GroupResponseDto.GroupDto addGroup(Member member, GroupRequestDto.AddGroupRequestDto requestDto);
    public GroupResponseDto.GroupDto enterGroup(Member member, GroupRequestDto.EnterGroupRequestDto requestDto);
    public MemberGroupResponseDto.MemberGroupInfoDto updateNickname(Member member, GroupRequestDto.AddGroupNicknameDto requestDto);
    public void leaveGroup(Member member, GroupRequestDto.LeaveGroupRequestDto requestDto);
    public void transferManager(Member member, GroupRequestDto.TransferManagerRequestDto requestDto);
    public void kickMember(Member member, GroupRequestDto.KickMemberRequestDto requestDto);
    public void inviteMember(Member member, GroupRequestDto.InviteMemberRequestDto requestDto);
    public void acceptInvitation(Member member, GroupRequestDto.InvitationActionRequestDto requestDto);
    public void rejectInvitation(Member member, GroupRequestDto.InvitationActionRequestDto requestDto);
}
