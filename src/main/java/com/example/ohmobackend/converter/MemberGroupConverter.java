package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.Group;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberGroup;
import com.example.ohmobackend.domain.enums.GroupRole;
import com.example.ohmobackend.web.dto.memberGroupDto.MemberGroupResponseDto;

public class MemberGroupConverter {

    public static MemberGroup toMemberGroupEntity(Member member, Group group, GroupRole groupRole) {
        return MemberGroup.builder()
                .role(groupRole)
                .group(group)
                .member(member)
                .build();
    }

    public static MemberGroupResponseDto.MemberGroupInfoDto toMemberGroupInfoDto(MemberGroup memberGroup) {
        return MemberGroupResponseDto.MemberGroupInfoDto.builder()
                .memberGroupId(memberGroup.getId())
                .role(memberGroup.getRole())
                .nickname(memberGroup.getNickname())
                .build();
    }
}
