package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.Diary;
import com.example.ohmobackend.domain.Group;
import com.example.ohmobackend.web.dto.groupDto.GroupRequestDto;
import com.example.ohmobackend.web.dto.groupDto.GroupResponseDto;

public class GroupConverter {

    public static Group toGroupEntity(GroupRequestDto.AddGroupRequestDto requestDto) {
        return Group.builder()
                .groupName(requestDto.getGroupName())
                .groupCode(requestDto.getGroupCode())
                .groupColor(requestDto.getGroupColor())
                .numPeople(requestDto.getNumPeople())
                .build();
    }

    public static GroupResponseDto.GroupDto toGroupDto(Group group) {
        return GroupResponseDto.GroupDto.builder()
                .groupName(group.getGroupName())
                .groupCode(group.getGroupCode())
                .groupColor(group.getGroupColor())
                .numPeople(group.getNumPeople())
                .build();
    }
}
