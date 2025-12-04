package com.example.ohmobackend.service.groupService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.groupDto.GroupRequestDto;
import com.example.ohmobackend.web.dto.groupDto.GroupResponseDto;

public interface GroupCommandService {

    public GroupResponseDto.GroupDto addGroup(Member member, GroupRequestDto.AddGroupRequestDto requestDto);
    public GroupResponseDto.GroupDto enterGroup(Member member, GroupRequestDto.EnterGroupRequestDto requestDto);
}
