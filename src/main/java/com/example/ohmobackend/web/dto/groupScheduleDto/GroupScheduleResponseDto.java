package com.example.ohmobackend.web.dto.groupScheduleDto;

import com.example.ohmobackend.web.dto.groupDto.GroupResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class GroupScheduleResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleAssigneeDto{
        private Long scheduleId;
        private List<GroupResponseDto.MemberDto> memberDtoList;
    }
}
