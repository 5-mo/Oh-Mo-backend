package com.example.ohmobackend.web.dto.groupDto;

import com.example.ohmobackend.web.dto.memberGroupDto.MemberGroupResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class GroupResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupDto{
        private Long groupId;
        private String groupName;
        private String groupCode;
        private String groupColor;
        private int numPeople;
        private String managerEmail;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupMembersDto{
        private Long groupId;
        private String groupName;
        private String groupCode;
        private String groupColor;
        private int numPeople;
        private List<MemberGroupResponseDto.MemberGroupInfoDto> memberGroupInfos;
    }

}
