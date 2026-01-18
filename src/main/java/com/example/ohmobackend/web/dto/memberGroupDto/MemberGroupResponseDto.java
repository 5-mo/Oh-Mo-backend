package com.example.ohmobackend.web.dto.memberGroupDto;

import com.example.ohmobackend.domain.enums.GroupRole;
import com.example.ohmobackend.web.dto.memberDto.MemberResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberGroupResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberGroupInfoDto{
        private MemberResponseDto.MemberInfoResponseDto memberInfo;
        private Long memberGroupId;
        private GroupRole role;
        private String nickname;
    }
}
