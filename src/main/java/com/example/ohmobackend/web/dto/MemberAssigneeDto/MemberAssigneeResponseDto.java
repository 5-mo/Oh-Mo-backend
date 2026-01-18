package com.example.ohmobackend.web.dto.MemberAssigneeDto;

import com.example.ohmobackend.web.dto.memberGroupDto.MemberGroupResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberAssigneeResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberAssigneeInfoResponseDto {
        private Long assigneeId;
        private boolean status;
        private MemberGroupResponseDto.MemberGroupInfoDto memberGroupInfo;
    }
}
