package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.Group;
import com.example.ohmobackend.domain.Member;
import com.example.ohmobackend.domain.MemberGroup;
import com.example.ohmobackend.domain.ScheduleAssignee;
import com.example.ohmobackend.web.dto.MemberAssigneeDto.MemberAssigneeResponseDto;
import com.example.ohmobackend.web.dto.groupDto.GroupRequestDto;
import com.example.ohmobackend.web.dto.groupDto.GroupResponseDto;
import com.example.ohmobackend.web.dto.memberGroupDto.MemberGroupResponseDto;

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
        List<MemberGroupResponseDto.MemberGroupInfoDto> memberGroupDtoList = memberGroupList.stream()
                .map(MemberGroupConverter::toMemberGroupInfoDto)
                .collect(Collectors.toList());

        return GroupResponseDto.GroupMembersDto.builder()
                .groupId(group.getId())
                .groupName(group.getGroupName())
                .groupCode(group.getGroupCode())
                .groupColor(group.getGroupColor())
                .numPeople(group.getNumPeople())
                .memberGroupInfos(memberGroupDtoList)
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

    public static MemberAssigneeResponseDto.MemberAssigneeInfoResponseDto toAssigneeDto(ScheduleAssignee assignee, MemberGroup memberGroup) {
        return MemberAssigneeResponseDto.MemberAssigneeInfoResponseDto.builder()
                .assigneeId(assignee.getId())
                .status(assignee.isStatus())
                .memberGroupInfo(MemberGroupConverter.toMemberGroupInfoDto(memberGroup))
                .build();
    }
}
