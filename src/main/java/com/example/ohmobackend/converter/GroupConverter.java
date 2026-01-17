package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.Group;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberGroup;
import com.example.ohmobackend.domain.ScheduleAssignee;
import com.example.ohmobackend.web.dto.groupDto.GroupRequestDto;
import com.example.ohmobackend.web.dto.groupDto.GroupResponseDto;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class GroupConverter {

    private static final String GROUP_CODE_CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int GROUP_CODE_LENGTH = 8;
    private static final SecureRandom secureRandom = new SecureRandom();

    public static Group toGroupEntity(GroupRequestDto.AddGroupRequestDto requestDto) {
        return Group.builder()
                .groupName(requestDto.getGroupName())
                .groupCode(generateGroupCode())
                .groupPassword(requestDto.getGroupPassword())
                .groupColor(requestDto.getGroupColor())
                .numPeople(requestDto.getNumPeople())
                .build();
    }

    public static GroupResponseDto.GroupDto toGroupDto(Group group) {
        return GroupResponseDto.GroupDto.builder()
                .groupId(group.getId())
                .groupName(group.getGroupName())
                .groupCode(group.getGroupCode())
                .groupColor(group.getGroupColor())
                .numPeople(group.getNumPeople())
                .build();
    }

    public static GroupResponseDto.GroupDto toGroupWithManagerDto(Group group, Member member) {
        return GroupResponseDto.GroupDto.builder()
                .groupId(group.getId())
                .groupName(group.getGroupName())
                .groupCode(group.getGroupCode())
                .groupColor(group.getGroupColor())
                .numPeople(group.getNumPeople())
                .managerEmail(member.getEmail())
                .build();
    }

    public static GroupResponseDto.GroupMembersDto toGroupMembersDto(Group group, List<MemberGroup> memberGroupList) {
        List<GroupResponseDto.MemberDto> memberDtoList = memberGroupList.stream()
                .map(memberGroup -> toMemberDto(memberGroup.getMember(), memberGroup.getNickname()))
                .collect(Collectors.toList());

        return GroupResponseDto.GroupMembersDto.builder()
                .groupId(group.getId())
                .groupName(group.getGroupName())
                .groupCode(group.getGroupCode())
                .groupColor(group.getGroupColor())
                .numPeople(group.getNumPeople())
                .memberDtoList(memberDtoList)
                .build();
    }

    private static String generateGroupCode() {
        StringBuilder sb = new StringBuilder(GROUP_CODE_LENGTH);
        for (int i = 0; i < GROUP_CODE_LENGTH; i++) {
            int index = secureRandom.nextInt(GROUP_CODE_CHARSET.length());
            sb.append(GROUP_CODE_CHARSET.charAt(index));
        }
        return sb.toString();
    }

    public static GroupResponseDto.MemberDto toAssigneeDto(ScheduleAssignee assignee) {
        return GroupResponseDto.MemberDto.builder()
                .email(assignee.getMemberGroup().getMember().getEmail())
                .nickname(assignee.getMemberGroup().getMember().getNickname())
                .assigneeId(assignee.getId())
                .groupNickname(assignee.getMemberGroup().getNickname())
                .status(assignee.isStatus())
                .build();
    }

    public static GroupResponseDto.MemberDto toMemberDto(Member member, String groupNickName) {
        return GroupResponseDto.MemberDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .groupNickname(groupNickName)
                .build();
    }
}
