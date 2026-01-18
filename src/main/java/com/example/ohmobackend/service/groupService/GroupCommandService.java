package com.example.ohmobackend.service.groupService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.groupDto.GroupRequestDto;
import com.example.ohmobackend.web.dto.groupDto.GroupResponseDto;
import com.example.ohmobackend.web.dto.memberGroupDto.MemberGroupResponseDto;

public interface GroupCommandService {

    public GroupResponseDto.GroupDto addGroup(Member member, GroupRequestDto.AddGroupRequestDto requestDto);
    public GroupResponseDto.GroupDto enterGroup(Member member, GroupRequestDto.EnterGroupRequestDto requestDto);
    public MemberGroupResponseDto.MemberGroupInfoDto updateNickname(Member member, GroupRequestDto.AddGroupNicknameDto requestDto);
}
