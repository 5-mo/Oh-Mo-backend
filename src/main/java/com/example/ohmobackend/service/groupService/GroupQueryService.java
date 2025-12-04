package com.example.ohmobackend.service.groupService;

import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.web.dto.groupDto.GroupResponseDto;

public interface GroupQueryService {

    public GroupResponseDto.GroupMembersDto getGroupMembers(Long groupId, Member member);
}
