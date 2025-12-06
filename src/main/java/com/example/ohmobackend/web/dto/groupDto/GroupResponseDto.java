package com.example.ohmobackend.web.dto.groupDto;

import com.example.ohmobackend.web.dto.memberDto.MemberResponseDto;
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
        private List<GroupResponseDto.MemberDto> memberDtoList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberDto {
        private String email;
        private String nickname;
        private Long assigneeId;
        private String groupNickname;
        private boolean status;
    }
}
