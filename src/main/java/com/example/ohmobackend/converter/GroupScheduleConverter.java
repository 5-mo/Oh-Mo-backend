package com.example.ohmobackend.converter;

import com.example.ohmobackend.domain.MemberGroup;
import com.example.ohmobackend.domain.Schedule;
import com.example.ohmobackend.web.dto.groupDto.GroupResponseDto;
import com.example.ohmobackend.web.dto.groupScheduleDto.GroupScheduleResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class GroupScheduleConverter {

    public static GroupScheduleResponseDto.ScheduleAssigneeDto toScheduleAssigneeDto(Schedule schedule, List<MemberGroup> memberGroupList) {
        List<GroupResponseDto.MemberDto> memberDtoList = memberGroupList.stream()
                .map(memberGroup -> GroupConverter.toMemberDto(memberGroup.getMember(), memberGroup.getNickname()))
                .collect(Collectors.toList());

        return GroupScheduleResponseDto.ScheduleAssigneeDto.builder()
                .scheduleId(schedule.getId())
                .memberDtoList(memberDtoList)
                .build();
    }
}
