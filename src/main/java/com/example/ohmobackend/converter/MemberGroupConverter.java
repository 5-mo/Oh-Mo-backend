package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.Group;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberGroup;
import com.example.ohmobackend.domain.enums.GroupRole;

public class MemberGroupConverter {

    public static MemberGroup toMemberGroupEntity(Member member, Group group, String nickname, GroupRole groupRole) {
        return MemberGroup.builder()
                .role(groupRole)
                .nickname(nickname)
                .group(group)
                .member(member)
                .build();
    }
}
