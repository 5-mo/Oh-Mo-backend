package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.Group;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberGroup;
import com.example.ohmobackend.domain.enums.GroupRole;

public class MemberGroupConverter {

    public static MemberGroup managerToMemberGroupEntity(Member member, Group group, String nickname) {
        return MemberGroup.builder()
                .role(GroupRole.MANAGER)
                .nickName(nickname)
                .group(group)
                .member(member)
                .build();
    }
}
